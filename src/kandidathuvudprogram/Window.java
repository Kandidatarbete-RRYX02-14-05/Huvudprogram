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
			case GAUSSIAN: 
				double sigma = 0.4;
				for(int i=0; i<length; i++){
					wind[i] = Math.pow(Math.E, -2*Math.pow((i-((length-1)/2))/(sigma*(length-1)),2));
				}
				return wind;
			
			default: 
				System.out.println("Bad String");
				throw new IllegalArgumentException("INVALID WINDOW :(");
		}

	}
	public enum Windows {

	    RECTANGULAR,
	    HAMMING,
	    COSINUS,
	    GAUSSIAN
	  }

}
