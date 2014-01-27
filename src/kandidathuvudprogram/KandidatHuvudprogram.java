/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kandidathuvudprogram;

import java.io.FileNotFoundException;
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
        
        
        // Testar Import.Java--------
        String fil = "G1_garb_111111-1s.tsf";
        Import imp = new Import();
        String data[] = null;
        try {
            data = imp.importera(800, 4000, fil);
        } catch (FileNotFoundException ex) {
            System.out.println("\n\n--Fil ej funnen: " + fil + " --\n\n");
            Logger.getLogger(KandidatHuvudprogram.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i=0; i<data.length; i++){
            System.out.println(data[i]);
        }
        //--------------------------
    }
}
