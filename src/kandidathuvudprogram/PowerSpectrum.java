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
    
    double yValues[], yValIntervals[][], spectrum[], window[], complexIntervals[][];

	private FFT fft;
    
    public PowerSpectrum(double [] yValues){
        initValues(1, yValues);        
        createIntervals();
        removeMean();
        //TODO:addfilter
        prepTransform("Hamming");
        transform();
        printArray(yValIntervals[0]);
        //TODO:removefilter
        //TODO:apply window
        Chart.useChart(spectrum);
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
    
    // gör invers fourier
    public void inverseDFT (double [] reArray, double [] imArray){
    	
    }
    
    //skapar Power spectrum
    public void transform(){
    	
    	//TODO: invertera Areal och Aimag dividera på N (FFTLength) vilket ger c(n)
    	//TODO: Applicera fönster på c(n)
    	//TODO: Göra FFT på c(n) och få C(k) där omega=2PI/L*k där L är 
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
       	
    	spectrum = new double [FFTLength];
    }
    
}
