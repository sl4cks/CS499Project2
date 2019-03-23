# CS499Project2
Build the rest of a synthesizer!


A) OSCILLATORS

You have been provided the class Blit.

1. Implement BlitSaw, a subclass of Blit.  In Tick, I suggest maintaining an 
	instance variable called prev, which holds the previous computed BlitSaw
	value, but returning a scaled version of prev.

2. Implement BPBlit, a subclass of Blit.  I suggest maintaining an internal 
	Blit called offPhaseBlit.  In addition to tick(), you'll also want to 
	implement setPhaseMod(), getPhaseMod(), and override setFrequencyMod().
	You should scale your BPBlit so it ranges 0...1.

3. Implement BlitSquare, a subclass of BPBlit.  You'll want to get the 
	frequency and period, which you can get by copying code from Blit.  
	In Tick, I suggest maintaining an instance variable called prev, which 
	holds the previous computed BlitSquare value, but returning a scaled 
	version of prev.

4. Compare BlitSaw to your existing sawtooth (Osc.java).  What problems does
	Osc.java have?

5. If you were really cool, you'd implement BlitTriangle, a subclass of 
	BlitSquare.  You'll want to get the frequency and period, which you 
	can get by copying code from Blit.  In Tick, I suggest maintaining an 
	instance variable called tprev (to be different from BlitSquare's prev),
	which holds the previous computed  BlitSquare value, but returning a 
	scaled version of tprev.

6. If you were really really cool, you'd implement a white noise source too.


B) FILTERS

1. Implement a Module called Filter.  Filter should have the following instance
	variables:

        Module input;
        double b0;
        double[] b;
        double[] a;
        double[] x;
        double[] y;
        double x0;

	Its constructor should be

	public Filter(Module input, double[] a, double[] b, double b0)

2. Implement a Filter subclass called LPF.  LPF take a FrequencyMod module
	to tell it the cutoff frequency and a ResonanceMod module to tell it the
	resonance.  Resonance should be 1/Sqrt(2) at 0.0 and, oh, I dunno,
        a lot higher, maybe 10 or so at 1.0, your choice.  Your LPF should implement 
        the 2-pole low-pass Butterworth filter in the lecture notes.  

3. If you were really cool, you'd implement an HPF, BandPassFilter, and NotchFilter.


C) MIXER

1. Implement a mixer which takes three inputs I1, I2, I3, and three amplitudes 
	A1, A2, A3, and performs I1 * A1 + I2 * A2 + I3 * A3.


D) SYNTH DEMO

1. Using your various modules and dials, show off a synthesizer with 2 or 3
	oscillators of your choice, which could be detuned and mixed, and then
	run through a filter and then an amplifier.  The filter and amplifier
	can both be controlled via envelopes.  You could modulate the pitch
	of the oscillators by an LFO.
