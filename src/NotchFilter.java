public class NotchFilter extends Filter{

    private Module cutoffModule =  new ConstantValue(Utils.hzToValue(100.0));  // default no cutoffModule
    private double cutoff;    // 2 * pi * (cutoff freq in Hz)
    private Module resonanceMod = new ConstantValue(0.0);
    private double Q = 1/Math.sqrt(2);   // default of no resonance

    public NotchFilter(Module input) {
        super(input, new double[] {0,0}, new double[] {0,0}, 1);
    }

    @Override
    public void setFrequencyMod(Module frequency) {
        this.cutoffModule = frequency;
        updateCutoff();
    }

    @Override
    public void setResonanceMod (Module resonance) {
        resonanceMod = resonance;
        updateQ();
    }

    private void updateQ() {
        Q = resonanceMod.getValue() * 10 + 1 / Math.sqrt(2);
    }

    // Updates and converts the cutoff frequency into radians ( Hz * 2 * pi)
    private void updateCutoff() {
        cutoff = Utils.valueToHz(cutoffModule.getValue()) * 2 * Math.PI;
    }

    private void updateCoefficients() {
        double K = cutoff * cutoff * Config.INV_SAMPLING_RATE * Config.INV_SAMPLING_RATE;
        double J = 4 * Q + (2 * cutoff * Config.INV_SAMPLING_RATE) + Q * K;

        // Calculate coefficients
        b0 = 1/J * Q*(4 + K) ;
        b[0] = 1/J * Q*(-8 + 2 * K);
        b[1] = 1/J * Q*(4 + K);
        a[0] = 1/J * (-8 * Q + Q * 2 * K);
        a[1] = 1/J * (4 * Q - 2 * cutoff * Config.INV_SAMPLING_RATE + Q * K);
    }

    public double tick(long tickCount) {
        updateQ();
        updateCutoff();
        updateCoefficients();
        return super.tick(tickCount);

    }

}
