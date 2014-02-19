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
    
    //skapar det som ska transformeras och transformerar sedan
    public void transform(double array[]){
    	int length=array.length;
    	double [] window=Window.createWindow(length,"Hamming");
    	double [] windowedFun = new double [2*length]; 		//resterande element borde vara 0
    	for (int i=0; i<length; i++){
    		windowedFun[i]=array[i]*window[i];
    	}
    	double [] emptyImaginary = new double [2*length];
    	FFT fft = new FFT(2*length);
    	fft.fft(windowedFun,emptyImaginary);
    	fft.printReIm(windowedFun,emptyImaginary);

    	
    			
    }
    
}
