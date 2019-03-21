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

   public void setInput(Module input1, Module input2, Module input3) {
        input[0] = input1;
        input[1] = input2;
        input[2] = input3;
   }

   public void setAmplitudes(Module amp1, Module amp2, Module amp3) {
        amplitudes[0] = amp1;
        amplitudes[1] = amp2;
        amplitudes[2] = amp3;
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
