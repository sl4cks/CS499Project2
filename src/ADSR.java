/*
 * Attack-Delay-Sustain-Release
 */

public class ADSR extends Module {

  MidiGate state; // 0 note off, 1 note pressed
  private int isPressed = 0;
  private int stage; //  Current stage. Attack=0, Decay=1, Sustain=2, Release=3, Done = 4
  private double defaultRates[] = {0.9999, 0.999, 1.0, 0.4, 1.0};    // defaults. stateRates[2] (sustain) and stateRates[4] (done) are always 1.0
  private double defaultStages[] = {.8, 0.5, 0.5, 0.0, 0.0};   // attack, decay, sustain, release, done levels. stageLevel[4] (done) always = 0
  private Module[] stageRates = new Module[5];
  private Module[] stageLevels = new Module[5];
  private double currentParameter = 0.0; //p
  private double startParameter = 0.0; //p'

  //need to figure when to reset these values
  private double s = 1.0; //s
  private final double epsilon = 1e-5; //a constant to handle denormals

  public ADSR(MidiGate gate) {
    this.state = gate;
    stage = 4; //default to done

    //initialize the stage rates and levels to 1 for all
    for(int i = 0; i < 5; i++) {
      stageRates[i] = new ConstantValue(defaultRates[i]);

      //make sure level for release/done are always 0
      stageLevels[i] = new ConstantValue(defaultStages[i]);
    }
  }

  public void setRate(int stage, Module input) {
    stageRates[stage] = input;
  }

  public void setLevel(int stage, Module input) {
    //sustain and decay have same target parameter
    if(stage == 1)
      stageLevels[2] = input;
    else if(stage == 2)
      stageLevels[1] = input;

    stageLevels[stage] = input;
  }

  public void notePress() {
    stage = 0;
    s = 1;
    startParameter = 0.0;
    currentParameter = 0.0;
    isPressed = 1;
  }

  public void noteRelease() {
    if (stage < 3) {
      stage = 3;
      s = 1;
      startParameter = currentParameter;
    }
    isPressed = 0;
  }

  //the update algorithm from notes
  public double update() {
    s = s * stageRates[stage].getValue()*(2 - stageRates[stage].getValue());
    if (s <= epsilon) {
      s = 1;
      startParameter = currentParameter;
      stage++;
    }
    currentParameter = s * startParameter + (1 - s) * stageLevels[stage].getValue();
    return currentParameter;
  }

  @Override
  public double tick(long tickCount) {
    //if these values match, the key hasn't been released/pressed
    if (state.getValue() == 1 && isPressed == 0) {
      notePress();
    }
    if (state.getValue() == 0 && isPressed == 1) {
      noteRelease();
    }
    return update();
  }
}
