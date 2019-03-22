public class Filter extends Module {

    // Note: Referencing Algorithm 13/14, Filters notes
    private Module input;
    private double b0 = 1;  // default
    private double[] b;
    private double[] a;
    private double[] x;  // for holding input at time i
    private double[] y;  // for holding output at time i
    private double x0;   // current input

    public Filter(Module input, double[] a, double[] b, double b0) {
        // Initialize x and y array elements all to zero
        x = new double[b.length];
        y = new double[a.length];

        this.input = input;
        this.a = a;
        this.b = b;
        this.b0 = b0;
    }

    // Algorithm 13
    // Unsure if this
    public double tick(long tickCount) {
        double sum = x0 * b0;

        for (int i = 0; i < a.length; i++) {
           sum -= a[i] * y[i];
        }
        for (int j = 0; j < b.length; j++) {
            sum += b[j] * x[j];
        }

        // Update the output arrays
        for (int i = a.length; i > 0; i--) {
            y[i] = y[i-1];
        }
        y[0] = sum;

        // Update the input arrays
        for (int j = b.length; j > 0; j--) {
            x[j] = x[j-1];
        }
        x[0] = x0;  // Update first input array element to most current input

        return sum;
    }
}
