// Copyright 2018 by George Mason University


import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

/**
 * DO NOT EDIT THIS SECTION BELOW (SEE BOTTOM OF FILES FOR WHERE TO UPDATE)
 * ============================== VVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
 **/
public class Synth {

    /**
     * Size of the Java SourceDataLine audio buffer. Larger and we have more latency.  Smaller and
     * the system can't handle it. It appears &geq 1024 is required on the Mac for 44100 KHz
     */
    public static final int BATCH_SIZE = 32;
    private Midi midi;
    // The current Mixer
    private Mixer.Info mixer;
    // The Audio Format
    private AudioFormat audioFormat;
    // The audio output
    private SourceDataLine sdl;
    // current write position in buffer
    private int buffpos = 0;
    // Audio buffer, which the audio output drains.
    private byte[] audioBuffer = new byte[BATCH_SIZE];
    /*
      Random Number Generation

      Each Sound has its own random number generator.
      You can get a new, more or less statistically  independent generator from this method.
    */
    private Object randomLock = new Object[0];
    private long randomSeed;
    private Random rnd = getNewRandom();
    private ArrayList<Module> modules = new ArrayList<Module>();
    private Module outputModule = null;
    // Global tickcount
    private long tickCount = 0;

    public Synth() {
        randomSeed = System.currentTimeMillis();
    }

    public void setOutput(Module outputModule) {
        this.outputModule = outputModule;
    }

    public Module getOutput() {
        return outputModule;
    }

    public void setMidi(Midi midi) {
        this.midi = midi;
    }

    public Midi getMidi() {
        return midi;
    }


    public static int getInt(String s) {
        try {
            return (Integer.parseInt(s));
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public static void showDevices(Midi midi, Mixer.Info[] mixers) {
        midi.displayDevices();
        System.err.println("\nAUDIO DEVICES:");
        for (int i = 0; i < mixers.length; i++) {
            System.err.println("" + i + ":\t" + mixers[i].getName());
        }

        System.err.println(
            "\nFormat:\n\tjava Synth\t\t\t[displays available devices]\n\tjava Synth [midi] [audio]\t[runs synth with the given device numbers]");
    }

    public static void main(String[] args) {
        Midi midi = new Midi();
        Synth synth = new Synth();
        synth.audioFormat = new AudioFormat(Config.SAMPLING_RATE, 16, 1, true, false);
        Mixer.Info[] mixers = synth.getSupportedMixers();

        if (args.length == 0) {
            showDevices(midi, mixers);
        } else if (args.length == 2) {
            int x = getInt(args[0]);
            ArrayList<Midi.MidiDeviceWrapper> in = midi.getInDevices();
            if (x >= 0 && x < in.size()) {
                midi.setInDevice(in.get(x));
                System.err.println("MIDI: " + in.get(x));

                if (mixers == null) {
                    System.err.println(
                        "No output found which supports the desired sampling rate and bit depth\n");
                    showDevices(midi, mixers);
                    System.exit(1);
                } else {
                    x = getInt(args[1]);
                    if (x >= 0 && x < mixers.length) {
                        synth.setMixer(mixers[x]);
                        System.err.println("Audio: " + mixers[x].getName());
                        synth.setMidi(midi);
                        synth.setup();
                        synth.go();
                        synth.sdl.drain();
                        synth.sdl.close();
                    } else {
                        System.err.println("Invalid Audio number " + args[1] + "\n");
                        showDevices(midi, mixers);
                        System.exit(1);
                    }
                }
            } else {
                System.err.println("Invalid MIDI number " + args[0] + "\n");
                showDevices(midi, mixers);
                System.exit(1);
            }
        } else {
            System.err.println("Invalid argument format\n");
            showDevices(midi, mixers);
            System.exit(1);
        }
    }

    /**
     * Returns the currently used Mixer
     */
    public Mixer.Info getMixer() {
        return mixer;
    }

    /**
     * Sets the currently used Mixer
     */
    public void setMixer(Mixer.Info mixer) {
        try {
            if (sdl != null) {
                sdl.stop();
            }
            if (mixer == null) {
                Mixer.Info[] m = getSupportedMixers();
                if (m.length > 0) {
                    mixer = m[0];
                }
            }
            if (mixer == null) {
                sdl = AudioSystem.getSourceDataLine(audioFormat);
            } else {
                sdl = AudioSystem.getSourceDataLine(audioFormat, mixer);
            }
            sdl.open(audioFormat, bufferSize);
            sdl.start();
            this.mixer = mixer;
        } catch (LineUnavailableException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the available mixers which support the given audio format.
     */
    public Mixer.Info[] getSupportedMixers() {
        DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        Mixer.Info[] info = AudioSystem.getMixerInfo();
        int count = 0;
        for (int i = 0; i < info.length; i++) {
            Mixer m = AudioSystem.getMixer(info[i]);
            if (m.isLineSupported(lineInfo)) {
                count++;
            }
        }

        Mixer.Info[] options = new Mixer.Info[count];
        count = 0;
        for (int i = 0; i < info.length; i++) {
            Mixer m = AudioSystem.getMixer(info[i]);
            if (m.isLineSupported(lineInfo)) {
                options[count++] = info[i];
            }
        }
        return options;
    }

    public static final int RANDOM_INCREASE = 17;

    Random getNewRandom() {
        synchronized (randomLock) {
            randomSeed += RANDOM_INCREASE;  // or whatever
            return new Random(randomSeed);
        }
    }

    /**
     * Sample ranges from 0 to 1
     */
    public void emitSample(double d) {
        int val = 0;
        if (d > 1.0) {
            d = 1.0;
        }
        if (d < 0.0) {
            d = 0.0;
        }
        d -= 0.5;

        val = (int) (d * 65536);
        if (val > 32767) {
            val = 32767;
        }
        if (val < -32768) {
            val = -32768;
        }

        audioBuffer[buffpos] = (byte) (val & 255);
        audioBuffer[buffpos + 1] = (byte) ((val >> 8) & 255);
        buffpos += 2;
        if (buffpos + 1 >= BATCH_SIZE) {
            sdl.write(audioBuffer, 0, buffpos);
            buffpos = 0;
        }
    }

    private void go() {
        if (this.outputModule == null) {
            System.err.println("No output module defined: exiting");
            return;
        }
        System.out.println("NO AUDIO FOR THE FIRST 3 SECONDS WHILE THE JIT KICKS IN!");
        while (true) {
            for (Module m : this.modules) {
                m.doUpdate(tickCount);
            }
            emitSample(this.outputModule.getValue());
            tickCount++;
        }
    }

    /**
     * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ============================== DO NOT EDIT THIS SECTION ABOVE
     **/

    // ADJUST THIS VALUE IF YOU GET A LOT OF GLITCHY SOUND
    private static int numSamples = 2048;

    // DON'T ADJUST THIS VALUE
    private static int bufferSize = numSamples * 2;

    /**
     * MAKE YOUR EDITS TO THIS METHOD
     **/
    public void setup() {
        // Default values for various modules:
        // No detuning = 0.5
        double pitchVal = 0.5547;     // overall pitch
        double stutterVal = 0.3461;
        double resonanceVal = 0.8945;
        double filterCutoffVal = 0.2234;

        // Detuning values for each oscillator
        double osc0DetuneVal = 0.5;
        double osc1DetuneVal = 0.7578;
        double osc2DetuneVal = 0.6289;

        // Mix values for each oscillator
        double osc0MixVal = 1.0;
        double osc1MixVal = 1.0;
        double osc2MixVal = 0.7813;

        // Set the default gain value
        double gainVal = 1.0;

        MidiModule midimod = new MidiModule(getMidi());
        modules.add(midimod);

        JFrame frame = new JFrame();
        Box box = new Box(BoxLayout.Y_AXIS);
        frame.setContentPane(box);

        // Get the status of the midi note being on or off
        MidiGate gate = new MidiGate(midimod);
        modules.add(gate);

        // Create an overall pitch/detuner control dial
        Detuner pitch = new Detuner(midimod);
        Dial pitchDial = new Dial(pitchVal);
        pitch.setDetuneMod(pitchDial.getModule());
        modules.add(pitch);

        // Create Detuners for each of our oscillators
        Detuner[] detuners = new Detuner[3];
        for (int i = 0; i < 3; i++) {
            detuners[i] = new Detuner(pitch);
            modules.add(detuners[i]);
        }

        // Create our three oscillators
        Blit blit1 = new BlitSaw();
        modules.add(blit1);

        Blit blit2 = new BlitSquare();
        modules.add(blit2);

        Blit blit3 = new BlitSaw();
        modules.add(blit3);

        Osc[] inputs = {blit1, blit2, blit3};
        String[] inputLabels = {"BlitSaw", "BlitSquare", "BlitSaw"};

        //iterate over each input and create dials/mixer for them
        MixerModule mixer = new MixerModule();
        for (int i = 0; i < inputs.length; i++) {
            Dial freq = new Dial(0.5);
            Dial mix = new Dial(1.0);

            // Update to default detune/mix value for osc 0
            if (i == 0) {
                freq.getModule().setValue(osc0DetuneVal);
                mix.getModule().setValue(osc0MixVal);
            }
            // Update to default detune/mix value for osc 1
            if (i == 1) {
                freq.getModule().setValue(osc1DetuneVal);
                mix.getModule().setValue(osc1MixVal);
            }
            // Update to default detune/mix value for osc 2
            if (i == 2) {
                freq.getModule().setValue(osc2DetuneVal);
                mix.getModule().setValue(osc2MixVal);
            }

            box.add(freq.getLabelledDial("Detune " + i + " - " + inputLabels[i]));

            //Link the dials to control our detuners for each oscillator
            detuners[i].setDetuneMod(freq.getModule());

            // Set out inputs to take the detuner-controlled frequency input
            inputs[i].setFrequencyMod(detuners[i]);

            // Add the mixer dials
            box.add(mix.getLabelledDial("Mixer " + i));
            mixer.setAmplitude(mix.getModule(), i);

            box.add(Box.createVerticalStrut(20));
        }

        mixer.setInput(inputs);
        modules.add(mixer);

        //the pattern of Blit makes this resemble a stutter pedal
        Blit trem = new Blit();
        modules.add(trem);

        // Add the dial for controlling the overall pitch/detune
        box.add(pitchDial.getLabelledDial("Pitch (master)"));

        //control the tremolo/stutter with dial
        Dial tremFreq = new Dial(stutterVal);
        box.add(tremFreq.getLabelledDial("Stutter"));
        trem.setFrequencyMod(tremFreq.getModule());

        Amplifier tremolo = new Amplifier(mixer);
        tremolo.setAmplitudeMod(trem);
        modules.add(tremolo);

        // Add filter dials
        Dial resonance = new Dial(resonanceVal);
        box.add(resonance.getLabelledDial("Resonance"));

        Dial LPFCutoff = new Dial(filterCutoffVal);
        box.add(LPFCutoff.getLabelledDial("Filter Cutoff Frequency"));

        Dial dial = new Dial(gainVal);
        box.add(dial.getLabelledDial("Gain"));

        // Create Filter and attach dial modules
        Filter filter = new LPF(tremolo);
        filter.setFrequencyMod(LPFCutoff.getModule());
        filter.setResonanceMod(resonance.getModule());
        modules.add(filter);


        Module s = new ConstantValue(0.0) ;
        modules.add(s);

        Oscilloscope oscope = new Oscilloscope();
        Oscilloscope.OModule omodule = oscope.getModule();
        oscope.setDelay(1);
        modules.add(omodule);
        box.add(oscope);

        box.add(Box.createVerticalStrut(10));

        Amplifier gateAmp = new Amplifier(filter);
        gateAmp.setAmplitudeMod(gate);
        modules.add(gateAmp);
        Amplifier gain = new Amplifier(gateAmp);
        gain.setAmplitudeMod(dial.getModule());
        modules.add(gain);

        ADSR adsr = new ADSR(gate);
        String[] labels = {"Attack Rate", "Decay", "Sustain", "Release"};
        for (int i = 0; i < 4; i++) {
            Dial dia = new Dial(0.5);
            box.add(dia.getLabelledDial(labels[i]));
            if(i == 2) {
                Dial attack = new Dial(0.5);
                box.add(attack.getLabelledDial("Attack Level"));
                adsr.setLevel(i, dia.getModule());
            }
            //check if we set sustain, a level
            else if(i == 2)
                adsr.setLevel(i, dia.getModule());
            else
                adsr.setRate(i, dia.getModule());
        }

        modules.add(adsr);


        omodule.setAmplitudeModule(gain);
        frame.pack();
        frame.setVisible(true);

        setOutput(gain);
    }
}