/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kandidathuvudprogram;

import javax.swing.JOptionPane;


/**
 * 
 * 
 */
public class PowerSpectrum {

	int intervalLength, FFTLength, dataLength;

	final int numberParts;

	double yValues[];

	double yValIntervals[][];

	double spectrum[][];

	double window[];

	double complexIntervals[][];

	double covariance[][];

	final double alpha;

	String windowName;

	private FFT fft;


	public static void main(String[] args){
		double[] sin = new double [100000];
		for (int i=0 ; i<sin.length; i++){
			sin[i]=Math.sin(i*400*Math.PI/sin.length);
		}
		double alpha=0.99;
		String windowName = "Rectangular";
		PowerSpectrum test = new PowerSpectrum(sin,alpha,windowName,1);
		Chart.useChart(test.getSpectrum(),"Sin",alpha,windowName);
	}

	public PowerSpectrum(double [] yValues, double alpha, int numberParts){
		this.yValues=yValues;
		this.alpha=alpha;
		this.numberParts=numberParts;
		initValues(yValues);
		removeMean();
		filter(yValues);
	}

	/**
	 * Gör power spectrum av datan specifierat enligt alpha, windowName och numberParts
	 * @param yValues Inputdata
	 * @param alpha Värdet på alpha i filtret
	 * @param windowName Namnet på fönstret som objektet initialiseras med.
	 * @param numberParts Antal delar som datan splittas upp i för spectrum
	 */
	public PowerSpectrum(double [] yValues, double alpha, String windowName, int numberParts){
		this.windowName=windowName;
		this.numberParts=numberParts;
		this.alpha=alpha;

		initValues(yValues);   
		removeMean();
		filter(yValues);
		createIntervals();
		prepTransform();     
		transform();
		try {
			applyWindow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
		fft.fft(spectrum[0], spectrum[1]);
		removeFilter(spectrum[0]);


		//---------------


	}

	// Sätter inmatade värden
	/**
	 * Sätter initiala värden på datal�ngd, antal delar och datav�rden
	 * @param yValues Datan som ska skapas spectrum av
	 */
	public void initValues(double yValues[]){
		this.yValues = yValues;
		this.dataLength =yValues.length;

	}

	public void setWindow(String newWindow){
		this.windowName=newWindow;
	}

	/**
	 * @return den reella componenten av vårt spektrum
	 */
	public double[] getSpectrum(){
		return spectrum[0];
	}
	
	public double getSpectrum(int i){
		return spectrum[0][i];
	}
	
	public double[] getRelevantSpectrum(){
		double minfreq=0.03;
		double maxfreq=0.3;
		int smallestElement=(int) (minfreq/(2*Math.PI/FFTLength));
		int largestElement=(int) Math.ceil((maxfreq/(2*Math.PI/FFTLength)));
		double[] relevantSpectrum = new double [smallestElement-largestElement];
		System.arraycopy(spectrum[0],smallestElement,relevantSpectrum,0,smallestElement-largestElement);
		return relevantSpectrum;
	}
				
	public String getWindowName(){
		return windowName;
	}
	
	public double getAlpha(){
		return alpha;
	}

	// Skapar array:en "yValIntervals" där första fältet är 
	public void createIntervals(){
		intervalLength = yValues.length / numberParts;
		FFTLength = nextPowerOf2(2*intervalLength);
		yValIntervals = new double[numberParts+1][FFTLength];	//vill ha en tom rad för att få Xn+1=0
		complexIntervals = new double[numberParts+1][FFTLength];// för n=numberParts
		for (int i=0; i<numberParts; i++){
			System.arraycopy(yValues, i*intervalLength, yValIntervals[i], 0, intervalLength);

		}  
	}

	// removes mean from data
	public void removeMean(){
		double sum = 0;
		for (int i = 0 ; i < dataLength; i++){
			sum += yValues[i];
		}
		sum = sum / dataLength;
		for (int i = 0 ; i < dataLength; i++){
			yValues[i] -= sum;
		}
	}

	// Skriver ut varje värde i en array
	public void printArray(double array[]){
		for (int i=0; i<array.length; i++){
			System.out.print("["+i+"]: " + array[i]);
		}
		System.out.println("");
	}

	//behöver räkna ut närmaste tvåpotens för FFT << är bitvis operation som i 
	//pricip fördubblar värdet i vårt fall.
	public static int nextPowerOf2(final int a)
	{
		int b = 1;
		while (b < a)
		{
			b = b << 1;
		}
		return b;
	}

	// definerar FFTLength etc så vi slipper göra det varje iteration
	public void prepTransform()
	{
		window = Window.createWindow(intervalLength,windowName);
		FFT fft = new FFT(FFTLength);
		this.fft = fft;
	}

	// multiplicerar ett tal med ett annats tals conjugat
	public double [] multiplyWithConjugate(double reOne, double imOne, 
			double reTwo, double imTwo){ //(a+bi)(c-di)=ac+bd+i(bc-ad)
		double [] temp = new double[2];
		temp[0]=reOne*reTwo + imOne*imTwo;
		temp[1]=imOne*reTwo - reOne*imTwo;
		return temp;
	}

	// inverterar en double array
	public void reverseArray(double [] array){

		for(int i = 0; i < array.length/2; i++)
		{
			double temp = array[i];
			array[i] = array[array.length - i - 1];
			array[array.length - i - 1] = temp;
		}

	}

	// gör invers fourier
	public void inverseFFT (double [] reArray, double [] imArray){
		fft.fft(reArray,imArray);
		reverseArray(reArray);
		reverseArray(imArray);
		double N=reArray.length;
		for (int i=0; i < N; i++){
			reArray[i]=reArray[i]/N;
			imArray[i]=imArray[i]/N;
		}

	}

	public void filter(double [] array){ //lägger på ett filter yi' = yi-ay(i-1)
		array[0]=0;
		for (int i = 1; i < array.length; i++){
			array[i] -= alpha*array[i-1];
		}
	}


	//ofärdig
	public void removeFilter(double [] array){
		for (int i = 0; i < array.length; i++){
			array[i] = array[i] / (1 - 2*alpha*Math.cos(2*Math.PI*i/FFTLength) + Math.pow(alpha,2)); 
		}
	}

	public static boolean isMaxToBig(double limit, double[] data){
		for(int k = 0; k < data.length; k++){
			if (data[k] > limit)
				return true;
		}
		return false;
	}

	//skapar Power spectrum
	public void transform() {
		for (int i = 0; i < numberParts; i++){
			fft.fft(yValIntervals[i],complexIntervals[i]);
		}

		double [] Areal = new double [FFTLength];
		double [] Aimag = new double [FFTLength];
		double [] temp1 = new double [2];
		double [] temp2 = new double [2];
		for (int i = 0; i < numberParts; i++){
			for (int k = 0; k < FFTLength; k++){	
				temp1=multiplyWithConjugate(yValIntervals[i][k], complexIntervals[i][k],
						yValIntervals[i][k], complexIntervals[i][k]); //(X_i)(X_i)*

						temp2=multiplyWithConjugate(yValIntervals[i][k], complexIntervals[i][k],
								yValIntervals[i+1][k], complexIntervals[i+1][k]); //(X_i)(X_{i+1})*

						Areal[k] += temp1[0] + Math.pow(-1, k)*temp2[0];
						Aimag[k] += temp1[1] + Math.pow(-1, k)*temp2[1];
			}
		}
		inverseFFT(Areal,Aimag);

		covariance = new double [2][FFTLength];
		//A blir nu covariansfunktionen.
		for (int i=0; i < Areal.length; i++){
			covariance[0][i] = Areal[i]/(numberParts*intervalLength);
			covariance[1][i] = Aimag[i]/(numberParts*intervalLength);
		}


	}

	public void applyWindow() throws Exception{
		//N=KM, K=numberParts
		int  M=intervalLength, L=FFTLength;
		spectrum = new double [2][FFTLength];
		//s=c(m)*w(m)  		0 <= m <= M-1
		for (int i=0; i <= M-1; i++){
			spectrum[0][i]=covariance[0][i]*window[i];
			spectrum[1][i]=covariance[1][i]*window[i];
		}


		//s= 0 				M <= m <= L-M
		for (int i=M; i<=L-M; i++){
			spectrum[0][i]=0;
			spectrum[1][i]=0;
		}

		//s= c(L-m)*w(L-m)	L-M+1 <= m <= L-1
		for (int i=L-M+1; i <= L-1; i++){
			spectrum[0][i]=covariance[1][L-i]*window[L-i];
			spectrum[1][i]=covariance[1][L-i]*window[L-i];
		}


		if (isMaxToBig(Math.pow(10, -10),spectrum[1])){
			throw new Exception("ERROR, imaginary vector is non-zero");
		}

	}

}
