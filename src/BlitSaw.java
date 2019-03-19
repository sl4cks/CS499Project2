public class BlitSaw extends Blit {

    private double prev = 0;

    public double tick(long tickCount) {
        double freq = Utils.valueToHz(getFrequencyMod().getValue());
        double p = Config.SAMPLING_RATE / freq;
        double alpha = 1 - (1/p);
        double val = alpha * prev + super.tick(tickCount) - (1/p);
        //change previous value to the new value
        prev = val;
        return val;
    }
}
