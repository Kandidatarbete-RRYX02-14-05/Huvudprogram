/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kandidathuvudprogram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class Havsdjup {
    
    public static void main(String[] args){
        Havsdjup djup = new Havsdjup();
       
        
        System.out.println("Lat: " + djup.lat[djup.lat.length-1] + " Lon: " + djup.lon[djup.lat.length-1] + " Depth: " + djup.depth[djup.lat.length-1]);
    }
    
    private double lat[], lon[], depth[];
    
    public Havsdjup(){
        initValues();
        
    }
    
    public void initValues(){
        String lines[], row[];
        lines = readFile("topo2.ascii");
        
        lat = new double[lines.length];
        lon = new double[lines.length];
        depth = new double[lines.length];
        
        for (int i=0; i<lines.length; i++){
            row = lines[i].split(" ");
            row = removeEmpty(row);
            lat[i] = Double.parseDouble(row[0]);
            lon[i] = Double.parseDouble(row[1]);
            depth[i] = Double.parseDouble(row[2]);
        }
    }
    public double[] getLatitude(){
        return lat;
    }
    public double[] getLongitude(){
        return lon;
    }
    public double[] getDepth(){
        return depth;
    }
    
    private String[] removeEmpty(String str[]){
        String newStr[];
        int count = 0;
        for (int i=0; i<str.length; i++){
            if (!str[i].equals("")){
                count++;
            }
        }
        newStr = new String[count];
        count = 0;

        for (int i=0; i<str.length; i++){
            if (!str[i].equals("")){
                newStr[count] = str[i];
                count++;
            }
        }
        return newStr;
    }
    
    private String[] readFile(String filnamn){
        FileInputStream fis = null;
        try {
            File file = new File(filnamn);
            fis = new FileInputStream(file);
            byte[] data = new byte[(int)file.length()];
            fis.read(data);
            fis.close();
            //
            return new String(data, "UTF-8").split("\n");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Havsdjup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Havsdjup.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Havsdjup.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public void printString(String[] str){
        for (int i=0; i<str.length; i++){
            System.out.println(str[i]);
        }
    }
    
    
}
