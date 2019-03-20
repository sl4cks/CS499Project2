public class Filter extends Module {

    private Module input;
    private double b0;
    private double[] b;
    private double[] a;
    private double[] x;
    private double[] y;
    private double x0;

    public double tick(long tickCount) {
        return 0.0;
    }
}
