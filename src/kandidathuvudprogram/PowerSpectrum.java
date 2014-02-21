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
    
    int numberParts, overlapLength, intervalLength, FFTLength;
    
    double yValues[], yValIntervals[][], spectrum[], window[];

	private FFT fft;
    
    public PowerSpectrum(double [] yValues){
        //testar funktioner
       /* double yValues[] = new double[2000]; 
        for (int i=0; i<2000; i++){
            yValues[i] = Math.sin(i*0.1);
        }*/
        initValues(1, 0, yValues);        
        createIntervals();
        /*for (int i=0; i<numberParts; i++){
           printArray(yValIntervals[i]);
        }*/
        prepTransform("Hamming");
        transform();       
        Chart.useChart(spectrum);
        //---------------
        
        
    }
    
    // Sätter inmatade värden
    public void initValues(int numberParts, int overlapLength, double yValues[]){
        this.numberParts = numberParts;
        this.overlapLength = overlapLength;
        this.yValues = yValues;
    } 
    // Skapar array:en "yValIntervals" där första fältet är 
    public void createIntervals(){
        intervalLength = (yValues.length+overlapLength*(numberParts-1))/numberParts;
        yValIntervals = new double[numberParts][intervalLength];
        
        for (int i=0; i<numberParts; i++){
            System.arraycopy(yValues, i*intervalLength, yValIntervals[i], 0, intervalLength);
        }
    }

    
    // Skriver ut varje värde i en array
    public void printArray(double array[]){
        for (int i=0; i<array.length; i++){
            System.out.print("["+i+"]: " + array[i]);
        }
        System.out.println("");
    }
    
    //behöver räkna ut närmaste tvåpotens för FFT << är bitvis operation som i pricip fördubblar i vårt fall.
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
        FFTLength = nextPowerOf2(2*intervalLength);
        FFT fft = new FFT(FFTLength);
        this.fft = fft;
    }
    
    
    //skapar det som ska transformeras och transformerar sedan
    public void transform(){
    	
    	double [] windowedFun = new double [FFTLength]; 		//resterande element borde vara 0
    	for (int i = 0; i<intervalLength; i++){
    		windowedFun[i] = yValues[i] * window[i];
    	}
    	double [] emptyImaginary = new double [FFTLength];
    					
    	fft.fft(windowedFun,emptyImaginary);
    	//fft.printReIm(windowedFun,emptyImaginary);
    	spectrum = new double [intervalLength];
    	double temp;
    	double sum = 0;
    	for (int i = 0; i < intervalLength; i++){			//ska det vara intervalLength???
    		temp=Math.pow(windowedFun[i], 2) + Math.pow(emptyImaginary[i], 2);
    		spectrum[i] = temp;
    		sum += temp;
    	}
    	sum=sum/intervalLength;
    	for (int i = 0; i < intervalLength; i++){
    		spectrum[i] -= sum;
    	}    			
    }
    
}
