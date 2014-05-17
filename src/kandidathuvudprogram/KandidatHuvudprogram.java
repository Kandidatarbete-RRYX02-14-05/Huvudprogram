/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kandidathuvudprogram;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.ml.train.MLTrain;

/**
 *
 * @author Server
 */
public class KandidatHuvudprogram {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {


		int[] nbrNeurons = {50};
		WaveCorrTest WTCsav = new WaveCorrTest(nbrNeurons);
		
		WTCsav.networkMultiTrain(150);
		WTCsav.saveNetwork(new File("NETWORKDONE"));
		
		
		WaveCorrTest WTC = new WaveCorrTest(new File("NETWORKDONE"));
		useNetwork.createFilesForAllDays(WTC);
		useNetwork.createGenErrorFiles(WTC);
		System.exit(0);

		/* 
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
        fft.fft(testdata1,testdata2); 
		//--------------
		 */ 
		//PowerSpectrum

		/*double alpha=0.99;
		String[] windowName={"hamming","bessel","rectangular"};
		for(int j=0; j<3; j++){
			

			String[] date ={"140106"};
			//PowerSpectrum testPower = new PowerSpectrum(sin,alpha,windowName,4);
			for(int i=0; i<4; i++){
				PowerSpectrum testPower = new PowerSpectrum(Filemanager.readGravFileInParts(date[0])[i],alpha,windowName[j],40);
				System.out.println(testPower.getRelevantSpectrum(1).length);
				Chart.useChart(testPower.getSpectrum(),date[0]+ "_40_"+i*6 + "h",testPower.getAlpha(),testPower.getWindowName());
			}
		}*/

	}
}
