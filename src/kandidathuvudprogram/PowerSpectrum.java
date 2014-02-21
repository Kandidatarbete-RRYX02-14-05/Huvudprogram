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
        //testar funktioner
       /* double yValues[] = new double[2000]; 
        for (int i=0; i<2000; i++){
            yValues[i] = Math.sin(i*0.1);
        }*/
        initValues(1, yValues);        
        createIntervals();
        /*for (int i=0; i<numberParts; i++){
           printArray(yValIntervals[i]);
        }*/
        removeMean();
        prepTransform("Hamming");
        transform();       
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
        yValIntervals = new double[numberParts][FFTLength];	
        complexIntervals = new double[numberParts][FFTLength];
        System.out.println(yValIntervals[0].length +" "  );
        
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
        FFT fft = new FFT(FFTLength);
        this.fft = fft;
    }
    
    
    //skapar det som ska transformeras och transformerar sedan
    public void transform(){
       	double[] transforms = new double[numberParts];
       	for (int i = 0; i < numberParts; i++){
    	fft.fft(yValIntervals[i],complexIntervals[i]);
       	}
       	
    	spectrum = new double [intervalLength];
    		// vill göra invers transform av
    		//Math.pow(yValIntervals[i], 2) + Math.pow(complexIntervals[i], 2))
    }
    
}
