public class BlitSaw extends Blit {

    private double prev = 0;

    public double saw(long tickCount) {
        double val;
        if(tickCount <= 0)
            val = 0;
        else {
            double freq = Utils.valueToHz(getFrequencyMod().getValue());
            //catch divide by 0
            if(freq == 0)
                return getValue();
            double p = Config.SAMPLING_RATE / freq;
            double alpha = 1 - (1 / p);
            val = alpha * prev + super.tick(tickCount) - (1 / p);
        }
        return val;
    }

    public double tick(long tickCount) {
        //update prev
        prev = saw(tickCount);
        //return scaled value
        return prev * 0.8 + 0.5;
    }
}
