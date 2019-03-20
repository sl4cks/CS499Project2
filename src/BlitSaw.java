public class BlitSaw extends Blit {

    private double prev = 0;

    public double tick(long tickCount) {
        double val;
        if(tickCount <= 0)
            val = 0;
        else {
            double freq = Utils.valueToHz(getFrequencyMod().getValue());
            double p = Config.SAMPLING_RATE / freq;
            double alpha = 1 - (1 / p);
            val = alpha * prev + super.tick(tickCount) - (1 / p);
        }
        //change previous value to the new value
        prev = val;
        return val;
    }
}
