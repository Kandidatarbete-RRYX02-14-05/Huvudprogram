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
    
    int numberParts, overlapLength;
    
    double yValues[], yValIntervals[][];
    
    public PowerSpectrum(){
        //testar funktioner
        double yValues[] = new double[2000]; 
        for (int i=0; i<2000; i++){
            yValues[i] = Math.sin(i*0.1);
        }
        initValues(6, 10, yValues);
        System.out.println(Math.pow(2,nextPowerOf2(2*12)));
        System.out.println(nextPowerOf2(5));

        
        createIntervals();
       // for (int i=0; i<numberParts; i++){
         //   printArray(yValIntervals[i]);
       // }
        transform(yValues);
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
        int intervalLength = yValues.length/numberParts;
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
    
    //behöver räkna ut närmaste tvåpotens för FFT
    public static int nextPowerOf2(final int a)
    {
        int b = 1;
        while (b < a)
        {
            b = b << 1;
        }
        return b;
    }
    
    
    //skapar det som ska transformeras och transformerar sedan
    public void transform(double array[]){
    	int length=array.length;
    	int FFTLength=nextPowerOf2(2*length);
    	double [] window=Window.createWindow(length,"Hamming");
    	
    	double [] windowedFun = new double [FFTLength]; 		//resterande element borde vara 0
    	for (int i=0; i<length; i++){
    		windowedFun[i]=array[i]*window[i];
    	}
    	double [] emptyImaginary = new double [FFTLength];
    	FFT fft = new FFT(FFTLength);				
    	fft.fft(windowedFun,emptyImaginary);
    	fft.printReIm(windowedFun,emptyImaginary);

    	
    			
    }
    
}
