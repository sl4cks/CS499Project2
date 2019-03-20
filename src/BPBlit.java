public class BPBlit extends Blit {

    private double duty = .5;
    private Blit offPhaseBlit = new Blit();

    public Module getPhaseMod() {
        return null;
    }

    public void setPhaseMod(Module mod) {
    }

    public double tick(long tickCount) {
        double val;
        if(tickCount <= 0)
            val = 0;
        else {
            double freq = Utils.valueToHz(getFrequencyMod().getValue());
            double p = Config.SAMPLING_RATE / freq;

            if(tickCount - p*duty < 0)
                val = super.tick((long)(tickCount+p*(1-duty)));
            else
                val = super.tick(tickCount) - offPhaseBlit.tick((long)(tickCount-p*duty));
        }
        //update previous, return new value
        return val;
    }
}
