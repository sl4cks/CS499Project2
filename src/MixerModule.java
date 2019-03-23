/*
 * Implement a mixer which takes three inputs I1, I2, I3, and three
 * amplitudes A1, A2, A3, and performs I1 * A1 + I2 * A2 + I3 * A3.
 */
public class MixerModule extends Module {

    private int size = 3;
    private Module[] input;
    private Module[] amplitudes;

    public MixerModule() {
        this.input = new Module[size];
        this.amplitudes = new Module[size];
   }

   public int getSize() {
        return this.size;
   }

   public void setInput(Module[] input) {
        this.input = input;
   }

   public void setInput(Module input, int i) {
        if(i >= size)
            throw new IllegalArgumentException("Input number exceeds size of mixer.");
        this.input[i] = input;
   }

   public void setAmplitude(Module[] amplitudes) {
        this.amplitudes = amplitudes;
   }

   public void setAmplitude(Module amp, int i) {
        this.amplitudes[i] = amp;
   }

    @Override
    public double tick(long tickCount) {
        double output = 0;
        for (int i = 0; i < size; i ++) {
            output += input[i].getValue() * amplitudes[i].getValue();
        }
        return output;
    }
}
