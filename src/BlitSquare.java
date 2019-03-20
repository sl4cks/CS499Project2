public class BlitSquare extends BPBlit {

    private double prev = 0;

    public double tick(long tickCount) {
        double val;
        if(tickCount <= 0)
            val = 0;
        else {
            double alpha = 0.999;
            double freq = Utils.valueToHz(getFrequencyMod().getValue());
            double p = Config.SAMPLING_RATE / freq;
            val = alpha * prev + super.tick(tickCount);
        }
        prev = val;
        return val;
    }
}
