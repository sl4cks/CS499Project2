public class Filter extends Module {

    private Module input;
    private double b0;
    private double[] b;
    private double[] a;
    private double[] x;
    private double[] y;
    private double x0;

    public Filter(Module input, double[] a, double[] b, double b0) {
        this.input = input;
        this.a = a;
        this.b = b;
        this.b0 = b0;
    }

    public double tick(long tickCount) {
        return 0.0;
    }
}
