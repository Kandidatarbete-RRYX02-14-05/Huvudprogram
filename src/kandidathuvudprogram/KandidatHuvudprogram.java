/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kandidathuvudprogram;

import java.io.File;
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

		kandidathuvudprogram.GetDataHgsChalmers.downloadGraviData("2010-06-10","2010-06-18");
		String[] dates = kandidathuvudprogram.GetDataHgsChalmers.generateDateString("2010-06-10","2010-06-18");


		// Testar Import.Java--------
		String fil = "gravidata/" + dates[2] + ".tsf"; // bytte för att fungera på linux
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

		double[] sin = new double[5000];
		for (int i=0; i<sin.length; i++){
			sin[i] = Math.sin(i*2*3.141592/1000);
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
		String windowName="Rectangular";
		// PowerSpectrum testPower = new PowerSpectrum(sin,alpha,windowName,4);
		PowerSpectrum testPower = new PowerSpectrum(testdata1,alpha,windowName,4);
		//Chart.useChart(sin,"Sin",alpha,windowName);
		Chart.useChart(testPower.getSpectrum(),fil.split("\\.")[0],testPower.getAlpha(),testPower.getWindowName());

		
	}

}
