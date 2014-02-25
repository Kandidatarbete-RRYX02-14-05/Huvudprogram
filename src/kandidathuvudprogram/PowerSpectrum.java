/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kandidathuvudprogram;


/**
 *
 * @author Server
 */
public class PowerSpectrum {
    
    int numberParts, intervalLength, FFTLength, dataLength;
    
    double yValues[], yValIntervals[][], spectrum[][], window[], complexIntervals[][];

	private FFT fft;
    
    public PowerSpectrum(double [] yValues){

        initValues(1, yValues);    
        removeMean();
        filter(yValues,0.9);
        createIntervals();
        prepTransform("Hamming");
        transform();
        removeFilter(spectrum[0],0.9);
        //TODO:removefilter
        //TODO:apply window
        Chart.useChart(spectrum[0]);
        //---------------
        
        
    }
    
    // Sätter inmatade värden
    public void initValues(int numberParts, double yValues[]){
        this.numberParts = numberParts;
        this.yValues = yValues;
        this.dataLength =yValues.length;
        
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
    public void prepTransform(String windowType)
    {
        window = Window.createWindow(intervalLength,windowType);
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

    public void filter(double [] array, double alpha ){ //lägger på ett filter yi' = yi-ay(i-1)
		for (int i = 1; i < array.length; i++){
		    array[i] -= alpha*array[i-1];
		}
    }
    
    
    //ofärdig
    public void removeFilter(double [] array, double alpha){
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
    public void transform(){
    	//N=KM, 
    		int K=numberParts, M=intervalLength, L=FFTLength;
    		
    	//DONE: invers FFT på  Areal och Aimag dividera på N  vilket ger c(n)
    	//DONE: Applicera fönster på c(n) så att 
    	
    			//s=c(m)*w(m)  		0 <= m <= M-1
    			//s= 0 				M <= m <= L-M
    			//s= c(L-m)*w(L-m)	L-M+1 <= m <= L-1
    	//TODO: Göra FFT på s(n) och få S(k) där omega=2PI/L*k
       	double[] transforms = new double[numberParts];
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
       	
       	spectrum = new double [2][FFTLength];
       	
       	//A blir nu covariansfunktionen.
       	for (int i=0; i < Areal.length; i++){
       		Areal[i] = Areal[i]/(numberParts*intervalLength);
       		Aimag[i] = Aimag[i]/(numberParts*intervalLength);
       	}
       	
       //	printArray(Areal);
       	//printArray(Aimag);
      //s=c(m)*w(m)  		0 <= m <= M-1
       	for (int i=0; i <= M-1; i++){
       		spectrum[0][i]=Areal[i]*window[i];
       		spectrum[1][i]=Aimag[i]*window[i];
       	}
       	
       	
      //s= 0 				M <= m <= L-M
       	for (int i=M; i<=L-M; i++){
       		spectrum[0][i]=0;
       		spectrum[1][i]=0;
       	}
       	
      //s= c(L-m)*w(L-m)	L-M+1 <= m <= L-1
       	for (int i=L-M+1; i <= L-1; i++){
       		spectrum[0][i]=Areal[L-i]*window[L-i];
       		spectrum[1][i]=Aimag[L-i]*window[L-i];
       	}
       	
       	if (isMaxToBig(Math.pow(10, -10),spectrum[1])){
       		System.out.println("ERROR, imaginary vector i non-zero");
       	}
       	fft.fft(spectrum[0], spectrum[1]);
       	
    }
    
}
