public class LPF extends Filter {

    private Module cutoff = new ConstantValue(0.0); // default

    public LPF(Module input, double[] a, double[] b, double b0) {
        super(input, a, b, b0);
    }
}
