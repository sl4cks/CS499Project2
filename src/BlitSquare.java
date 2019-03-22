public class BlitSquare extends BPBlit {

    private double prev = 0;

    public double square(long tickCount) {
        double alpha = 0.999;
        double val = alpha * prev + super.bpblit(tickCount);
        return val;
    }

    public double tick(long tickCount) {
        //update prev, probably wrong
        prev = square(tickCount);
        return prev;
    }
}
