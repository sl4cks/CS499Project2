public class LPF extends Filter {

    /*
     --- variables from Filter:

    private Module input;
    private double b0 = 1;  // default
    private double[] b;
    private double[] a;
    private double[] x;  // for holding input at time i
    private double[] y;  // for holding output at time i
    private double x0;   // current input
     */
    private Module cutoffModule =  new ConstantValue(Utils.hzToValue(100.0));  // default no cutoffModule
    private double cutoff;    // 2 * pi * (cutoff freq in Hz)
    private Module resonanceMod = new ConstantValue(0.0);
    private double Q = 1/Math.sqrt(2);   // default of no resonance

    public LPF(Module input) {
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
        double K = cutoff * cutoff * Config.INV_SAMPLING_RATE * Config.INV_SAMPLING_RATE * Q;
        double J = 4 * Q + (2 * cutoff * Config.INV_SAMPLING_RATE) + K;

        // Calculate coefficients
        b0 = 1/J * K;

        b[0] = 1/J * 2 * K;
        b[1] = 1/J *  K;
        a[0] = 1/J * (-8 * Q + 2 * K);
        a[1] = 1/J * (4 * Q - 2 * cutoff * Config.INV_SAMPLING_RATE + K);
    }

    public double tick(long tickCount) {
        updateQ();
        updateCutoff();
        updateCoefficients();
        return super.tick(tickCount);

    }

}
