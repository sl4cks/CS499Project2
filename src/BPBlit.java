public class BPBlit extends Blit {

    private double duty = .5;
    private double prev = 0;
    final private double alpha = 0.999;

    public double tick(long tickCount) {
        double val;
        if(tickCount == 0)
            val = 0;
        else {
            double freq = Utils.valueToHz(getFrequencyMod().getValue());
            double p = Config.SAMPLING_RATE / freq;
            val = alpha * prev + super.tick(tickCount) - super.tick((long) (tickCount - p * duty)); //may call blit with negative
        }
        //update previous, return new value
        prev = val;
        return val;
    }
}
