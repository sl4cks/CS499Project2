public class BPBlit extends Blit {

    private double duty = .5;
    private Blit offPhaseBlit = new Blit();

    public double getPhaseMod() {
        return this.duty;
    }

    public void setPhaseMod(double phaseMod) {
        this.duty = phaseMod;
    }

    public void setFrequencyMod(Module module) {
        super.setFrequencyMod(module);
        offPhaseBlit.setFrequencyMod(module);
    }

    public double bpblit(long tickCount) {
        return super.tick(tickCount) - offPhaseBlit.tick(tickCount, getPhaseMod());
    }

    public double tick(long tickCount) {
        return bpblit(tickCount)*0.5+0.5;
    }
}
