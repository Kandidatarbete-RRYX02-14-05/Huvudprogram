package kandidathuvudprogram;
/*  
 *  Skapar en array av doubles med det valda fönstret.
 *  Valbara fönster: Rectangular, Hamming, Cosinus, Gaussian.
 */

public class Window {


	public static double[] createWindow(int length, String window) throws IllegalArgumentException {

		double[] wind = new double[length];
		Windows type = Windows.valueOf(window.toUpperCase());

		switch (type){
		case RECTANGULAR: 

			for(int i=0; i<length; i++){
				wind[i] = 1;
			}
			return wind;
		case HAMMING: 
			for(int i=0; i<length; i++){
				wind[i] = 0.54-0.46*Math.cos(2*Math.PI*i/(length-1));
			}
			return wind;
		case COSINUS: 
			for(int i=0; i<length; i++){
				wind[i] = Math.sin((Math.PI*i)/(length-1));
			}
			return wind;
		case HANNING:
			for(int i=0; i<length; i++){
				wind[i] = 0.5*(1-Math.cos((2*Math.PI*i)/(length-1)));
			}
			return wind;
		case GAUSSIAN: 
			double sigma = 0.4;
			for(int i=0; i<length; i++){
				wind[i] = Math.pow(Math.E, -2*Math.pow((i-((length-1+0.0)/2))/(sigma*(length-1)),2));
			}
			return wind;
		case BESSEL: 
			for (int i = 0; i<length; i++){
				wind[i] = ModBesselFunctions.bessi0(Math.PI*3*Math.sqrt(1.0-Math.pow((2*i+0.0)/(length-1)-1, 2)))/ModBesselFunctions.bessi0(Math.PI*3);
			}
			return wind;
		
		default: 
			throw new IllegalArgumentException("INVALID WINDOW :( - Du har antagligen glömt att ange alpha");
		}

	}

	public static double[] createWindow(int length, String window, double alpha) throws IllegalArgumentException {

		double[] wind = new double[length];
		Windows type = Windows.valueOf(window.toUpperCase());
		switch (type){
		case BESSEL: 
			for (int i = 0; i<length; i++){
				wind[i] = ModBesselFunctions.bessi0(Math.PI*alpha*Math.sqrt(1.0-Math.pow(2*i/(length-1)-1, 2)))/ModBesselFunctions.bessi0(Math.PI*alpha);
			}
			return wind;
		default: 
			throw new IllegalArgumentException("INVALID WINDOW :( - du har antagligen ett alpha här som inte ska vara med.");
		}
	}
		public enum Windows {

			RECTANGULAR,
			HAMMING,
			COSINUS,
			GAUSSIAN, 
			HANNING,
			BESSEL
		}

	}
