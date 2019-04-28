public class BlitSquare extends BPBlit {

    private double prev = 0;
    private double alpha = 0.999;

    public double square(long tickCount) {
        return alpha * prev + bpblit(tickCount);
    }

    public double tick(long tickCount) {
        //update the previous value
        prev = square(tickCount);
        return prev * .9 + this.getPhaseMod();
    }
}
