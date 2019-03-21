public class LPF extends Filter {

    private Module cutoff =  new ConstantValue(Utils.hzToValue(0));  // default no cutoff
    private Module resonanceMod = new ConstantValue(0.0);

    public LPF(Module input, double[] a, double[] b, double b0) {
        super(input, a, b, b0);
    }

    public void setCutoff(Module frequency) {
        this.cutoff = frequency;
    }

    public void setResonance (Module resonance) {
        this.resonanceMod = resonance;
    }

    // Returns the resonance (Q) as a value from 1/sqrt(2) to ~10.0
    public double getQ() {
        return resonanceMod.getValue() * 10 + 1 / Math.sqrt(2);
    }
   // Algorithm 14

}
