/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kandidathuvudprogram;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Server
 */
public class KandidatHuvudprogram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
       // kandidathuvudprogram.GetDataHgsChalmers.downloadGraviData("2010-04-10","2010-04-11");
        String[] dates = kandidathuvudprogram.GetDataHgsChalmers.generateDateString("2010-04-10","2010-04-15");
       
   
        // Testar Import.Java--------
        String fil = "gravidata\\" + dates[0] + ".tsf";
        Import imp = new Import();
        String dataTime[], dataValue[];  // tid vid varje värde samt värde vid varje tid...
        
        dataTime = imp.importWhole(fil);
        dataValue = new String[dataTime.length];
        String[] temp;
        for (int i=0; i<dataTime.length-1; i++){
            temp = dataTime[i].split(" ");
            dataValue[i] = temp[temp.length-1];
            dataTime[i] = temp[0];
        }
        

        
        //--------------------------
        // testar fft
        double testdata1[] = new double[dataTime.length-1];
        for (int i=0; i<testdata1.length; i++){
        	testdata1[i] = Double.parseDouble(dataValue[i]);
        };
        // System.out.println(data[0].split(" ")[0]);
        
        /*for (int i=0; i<data.length; i++){
            testdata1[i] = Double.parseDouble(data[i].split("\t")[1].replace(" ", ""));
            testdata2[i] = Double.parseDouble(data[i].split("\t")[6]);
        }
        FFT fft = new FFT(2);
        fft.fft(testdata1,testdata2); */
        //--------------
        
        //PowerSpectrum
        double alpha=0.99;
        String windowName="Hanning";
        PowerSpectrum testPower = new PowerSpectrum(testdata1,alpha,windowName,4);
        Chart.useChart(testPower.getSpectrum(),fil.split("\\.")[0],alpha,windowName); 
        
    }
    
}
