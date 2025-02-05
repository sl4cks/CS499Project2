public class Blit extends Osc {

	public double sincm(double x, double m) {
		double v = m * Math.sin(Math.PI * x / m);
		if (v == 0) return 1;
		return Math.sin(Math.PI * x) / v;
	}
	
	double phase = 0;
	
	public double tick(long tickCount) {
		return tick(tickCount, 0);
	}
		
		
	// Note that BLIT will generate values below 0.  That's okay I guess, we're not using it
	// as a serious audio output source, just scaled derivatives of it like saw.

    protected double tick(long tickCount, double d) {
    	double freq = Utils.valueToHz(getFrequencyMod().getValue());
		if(freq == 0)
		    return 0;
    	double p = Config.SAMPLING_RATE / freq;
    	double m = Math.floor(p / 2.0) * 2.0 + 1.0;
    	double val = (m /p) * sincm((phase - d * p ) * m/p, m);
    	phase++ ;
    	if (phase >= p) phase -= p;
    	return val;
    	}
   }
