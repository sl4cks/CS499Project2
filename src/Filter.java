public class Filter extends Module {

    // Note: Referencing Algorithm 13/14, Filters notes
    private Module input;
    private double b0;
    private double[] b;
    private double[] a;
    private double[] x;  // for holding input at time i
    private double[] y;  // for holding output at time i
    private double x0;   // current input

    public Filter(Module input, double[] a, double[] b, double b0) {
        // Initialize x and y array elements all to zero
        x = new double[a.length];
        y = new double[b.length];

        this.input = input;
        this.a = a;
        this.b = b;
        this.b0 = b0;
    }

    public double tick(long tickCount) {
        return 0.0;
    }
}
