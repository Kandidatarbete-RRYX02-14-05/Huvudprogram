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
        
        
        
        // Testar TestChart ---
        TestChart hej = new TestChart();
        // ----------------
        
        
        // Testar Import.Java--------
        String fil = "Datafil111111.tsf";
        Import imp = new Import();
        String data[] = null;
        try {
            data = imp.importera(0, 1000, fil);
        } catch (FileNotFoundException ex) {
            System.out.println("\n\n--Fil ej funnen: " + fil + " --\n\n");
            Logger.getLogger(KandidatHuvudprogram.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //--------------------------
        // testar fft
        double testdata1[] = new double[data.length];
        double testdata2[] = new double[data.length];
        for (int i=0; i<testdata1.length; i++){
        	testdata1[i]=Double.parseDouble(data[i]);
        };
        // System.out.println(data[0].split(" ")[0]);
        
        /*for (int i=0; i<data.length; i++){
            testdata1[i] = Double.parseDouble(data[i].split("\t")[1].replace(" ", ""));
            testdata2[i] = Double.parseDouble(data[i].split("\t")[6]);
        }
        FFT fft = new FFT(2);
        fft.fft(testdata1,testdata2);*/
        //--------------
        
        //PowerSpectrum
        PowerSpectrum testPower = new PowerSpectrum(testdata1);
    }
}
