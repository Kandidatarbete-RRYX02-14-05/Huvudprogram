/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kandidathuvudprogram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Server
 */
public class Import {
    
    public Import(){
        
    }
    
    //Läser fil från specifik rad och returnerar rader som ett "strängfält"
    public String[] importera(int radStart, int antalRader, String filnamn) throws FileNotFoundException{
        
        String data[] = new String[antalRader];
        FileReader filLas = new FileReader(filnamn);
        BufferedReader bfLas = new BufferedReader(filLas);
        
        //Läser till den rad som läsningen skall starta på(radStart)
        for (int i=0; i<radStart; i++){
            try {
                bfLas.readLine();
            } catch (IOException ex) {
                System.out.println("\n\n--Fel i läsning av fil vid rad: " + i + " --\n\n");
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Läser från radStart det antal rader som specificerats
        for (int i=0; i<antalRader; i++){
            try {
                data[i] = bfLas.readLine();
            } catch (IOException ex) {
                System.out.println("\n\n--Fel i läsning av fil vid rad: " + i + " --\n\n");
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return data;
    }
            
}