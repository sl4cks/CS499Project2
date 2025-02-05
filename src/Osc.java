/*
 * Utility module that provides some common parameters for all modules inteded to be oscillators
 */
public class Osc extends Module {
  public double state = 0;


  private Module frequencyMod = new ConstantValue(Utils.hzToValue(220));

  public void setFrequencyMod(Module frequencyMod) {
    this.frequencyMod = frequencyMod;
  }

  public Module getFrequencyMod() {
    return this.frequencyMod;
  }

  public double tick(long tickCount) {
    double hz = Utils.valueToHz(getFrequencyMod().getValue());
    state += hz * Config.INV_SAMPLING_RATE;
    if (state >= 1)
      state -= 1;
    return state;
  }

}
