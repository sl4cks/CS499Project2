public class BlitSquare extends BPBlit {

    private double prev = 0;

    public double square(long tickCount) {
        double alpha = 0.999;
        //super updates prev and gives bpblit value
        return alpha * prev + super.bpblit(tickCount);
    }

    public double tick(long tickCount) {
        //update the previous value
        prev = square(tickCount);
        return  prev * .75 + super.getPhaseMod();
    }
}
