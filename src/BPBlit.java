public class BPBlit extends Blit {

    private double duty = .5;

    public double tick(long tickCount) {
        double val;
        if(tickCount <= 0)
            val = 0;
        else {
            double freq = Utils.valueToHz(getFrequencyMod().getValue());
            double p = Config.SAMPLING_RATE / freq;
            val = super.tick(tickCount) - super.tick((long)(tickCount-p*duty));
        }
        //update previous, return new value
        return val;
    }
}
