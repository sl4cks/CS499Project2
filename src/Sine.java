public class Sine extends Osc {

  @Override
  //convert the output from the Osc tick to a sin wave
  public double getValue() {
      return (Utils.fastSin(super.getValue()*2*Math.PI)+1)/2;
  }
}
