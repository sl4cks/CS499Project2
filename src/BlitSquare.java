public class BlitSquare extends BPBlit {

    private double prev = 0;

    public double square(long tickCount) {
        double alpha = 0.999;
        double val = alpha * prev + super.bpblit(tickCount);
        prev = val;
        return val;
    }

    public double tick(long tickCount) {
        return square(tickCount) * .75 + phase;
    }
}
