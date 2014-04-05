/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kandidathuvudprogram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class Import {

	public Import(){

	}

	/**
	 * Läser fil från specifik rad och returnerar rader som ett "strängfält". Returner tomt fält om nått gått fel.
	 * @param radStart
	 * @param antalRader
	 * @param filnamn
	 * @return String rows[]
	 */
	public String[] importera(int radStart, int antalRader, String filnamn){
		FileReader filLas = null;
		try {
			String data[] = new String[antalRader];
			filLas = new FileReader(filnamn);
			BufferedReader bfLas = new BufferedReader(filLas);

			//Läser till den rad som läsningen skall starta på(radStart)
			for (int i=0; i<radStart; i++){
				try {
					bfLas.readLine();
				} catch (IOException ex) {
					System.out.println("\n\n--Fel i läsning av fil vid rad: " + i + " --\n\n");
					Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
				}
			}   //Läser från radStart det antal rader som specificerats
			for (int i=0; i<antalRader; i++){
				try {
					data[i] = bfLas.readLine();
				} catch (IOException ex) {
					System.out.println("\n\n--Fel i läsning av fil vid rad: " + i + " --\n\n");
					Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
				}
			}   

			return data;
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				filLas.close();
			} catch (IOException ex) {
				Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return new String[] {""};
	}

	/**
	 * 
	 * @param filnamn
	 * @return number of lines in file
	 */
	public int numberOfLinesInFile(String filnamn){
		int lines = 0;
		try {
			FileReader filLas = new FileReader(filnamn);
			BufferedReader bfLas = new BufferedReader(filLas);         

			//Läser till den rad som läsningen skall starta på(radStart)
			while (bfLas.readLine() != null){
				lines++;
			}
		} catch (IOException ex) {
			Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
		}

		return lines;
	}

	/**
	 * Importerar en hel fil där varje rad hamnar i en sträng i ett strängfält...
	 * @param filnamn
	 * @return String rows[] of files
	 */
	public String[] importWhole(String filnamn){
		FileInputStream fis = null;
		try {
			File file = new File(filnamn);
			fis = new FileInputStream(file);
			byte[] data = new byte[(int)file.length()];
			fis.read(data);
			fis.close();

			return new String(data, "UTF-8").split("\n",-1);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
				Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return new String[] {""};
	}
/**
 * Returnerar en fil 
 * @param filnamn
 * @return String [4][3600*6]
 */
	public String[][] splitSixHours(String data){
		String[][] parts = new String [4][3600*6];
		for (int i=0; i<4; i++){
			System.arraycopy(data,i*21600,parts[i],0,21600);
		}
		return parts;
	}

}