/*
    Module that detunes an input frequency up/down an octave
 */
public class Detuner extends Module{

    private Module frequencyMod;
    private Module detuneMod = new ConstantValue(0.5); // default no detuning

    public Detuner(Module frequencyMod) {
        this.frequencyMod = frequencyMod;
    }

    public void setDetuneMod(Module detuneMod) {
        this.detuneMod = detuneMod;
    }

    private double getDetunedFreq() {
        double x = detuneMod.getValue();
        double a = x / 1.18436;
        // This maps the range 0.0 - 1.0 to 0.5 - 2.0
        // We'll use this to adjust the pitch of the oscillator by multiplying
        //    the oscillator's frequency one octave up or down.
        double y = 4.152815 + (0.5 - 4.152815)/(1 + Math.pow(a, 2.135379));
        return frequencyMod.getValue() * y;
    }

    public double tick(long tickCount) {
        return getDetunedFreq();
    }
}
