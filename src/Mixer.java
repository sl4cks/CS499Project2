/*
 * Implement a mixer which takes three inputs I1, I2, I3, and three
 * amplitudes A1, A2, A3, and performs I1 * A1 + I2 * A2 + I3 * A3.
 */


public class Mixer extends Module {

    private int size = 3;
    private Module[] input;
    private Module[] amplitudes;

    public Mixer() {
        this.input = new Module[size];
        this.amplitudes = new Module[size];
   }

   public void setInput(Module[] input) {
        this.input = new Module[input.length];
        for (int i = 0; i < input.length; i++) {
            this.input[i] = input[i];
        }
   }

   public void setAmplitudes(Module[] amplitudes) {
        this.amplitudes = new Module[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            this.amplitudes[i] = amplitudes[i];
        }
   }

    @Override
    public double tick(long tickCount) {
        double output = 0;
        for (int i = 0; i < size; i ++) {
            output += input[i].getValue() * amplitudes[i].getValue();
        }
        setValue(output);
        return output;
    }
}
