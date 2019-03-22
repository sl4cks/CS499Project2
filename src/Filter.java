public class Filter extends Module {

    // Note: Referencing Algorithm 13/14, Filters notes
    public Module input;
    public double b0 = 1;  // default
    public double[] b;
    public double[] a;
    public double[] x;  // for holding input at time i
    public double[] y;  // for holding output at time i
    public double x0;   // current input

    public Filter(Module input, double[] a, double[] b, double b0) {
        // Initialize x and y array elements all to zero
        x = new double[b.length];
        y = new double[a.length];

        this.input = input;
        this.a = a;
        this.b = b;
        this.b0 = b0;
    }

    public double getCurrentInput() {
        return this.x0;
    }

    public double getB0() {
        return this.b0;
    }

    public void setA(double[] a) {
        for (int i = 0; i < a.length; i ++) {
            this.a[i] = a[i];
        }
    }

    public void setB(double[] b) {
        for (int i = 0; i < b.length; i ++) {
            this.b[i] = b[i];
        }
    }

    public void setB0(double b0) {
        this.b0 = b0;
    }

    // Algorithm 13
    public double tick(long tickCount) {
        // Get incoming frequency, convert to hz
        x0 = Utils.valueToHz(input.getValue());
        double sum = x0 * b0;

        //Calculate new output using a and b coefficients
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

        return Utils.valueToHz(sum);
    }
}
