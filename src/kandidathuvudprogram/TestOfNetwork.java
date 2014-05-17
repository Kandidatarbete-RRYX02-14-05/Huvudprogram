/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kandidathuvudprogram;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Markus
 */
public class TestOfNetwork {
	public static void main(String[] args){
		TestOfNetwork test = new TestOfNetwork("Network-NbrNeurons-50-NbrIterations-50");
		//test.testOne()
		String[] naame = {"AtlanticCenter","Biscay", "CelticShelf", "Groenlandsudden", "Gul", "Iceland", "Kust", "NorthSea", "NorwegianSea", "Rod"};
		for(int i = 0; i< naame.length; i++){
		test.testArea("NetworkTest/" + naame[i] + ".txt", naame[i]);
		}
		System.exit(0);
	}
	File networkFile;

	public TestOfNetwork(String networkFileName){
		networkFile = new File(networkFileName);    
	}

	public TestOfNetwork(int numberNeurons, int trainIterations){
		WaveCorrTest network = new WaveCorrTest("TrainDatum1.txt","genDatum1.txt", new int[]{numberNeurons}, true,0.99, "bessel", 0, 0, 0, "resilientpropagation","activationsigmoid");
		for (int i=0; i<trainIterations; i++){
			network.networkTrain();
		}

		networkFile = new File("Network-NbrNeurons-" + numberNeurons + "-NbrIterations-" + trainIterations);
		network.saveNetwork(networkFile); 
	}

	public void testArea(String fileName, String testName){
		WaveCorrTest WTC = new WaveCorrTest(networkFile);// load(network);
		Import imp = new Import();

		String[] pointStr = imp.importWholeComma(fileName);
		int[] nbrsToTry;
		Point[] pts = new Point[pointStr.length];

		for (int i=0; i<pointStr.length/2-1; i++){
			pts[i] = new Point(Integer.parseInt(pointStr[i*2]), Integer.parseInt(pointStr[i*2+1])); 
		}
		nbrsToTry = Filemanager.choosePoints(pts);
		for (int waveH=0; waveH<15; waveH++) {
			try {
				System.out.println("NetworkTest/NetData/NetworkTest-" + testName + "-waveH-" + waveH + ".txt");
				Utskrift.write("NetworkTest/NetworkTest-" + testName + "-waveH-" + waveH + ".txt",WTC.fakeWaveTest(nbrsToTry, waveH));
			} catch (IOException ex) {
				Logger.getLogger(TestOfNetwork.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	// lat 1,35
	// lon -70,15
	public void testOne() { //nyttnamn

		int latStart = 35,
				latStop = 75,
				lonStart = -70,
				lonStop = 15;
		int stepLength = 5;

		double waveMin = 0;
		double waveMax = 14;
		double waveStep = 1;

		WaveCorrTest WTC = new WaveCorrTest(networkFile);// load(network);

		int IdealDataSize = (WTC.buffSet.get(0).getIdeal()).getData().length;

		int i = lonStart - stepLength;
		int j;
		double k;
		while (i < lonStop + stepLength){
			j = latStart - stepLength;
			while(j < latStop + stepLength){
				k = waveMin;
				while (k <= waveMax){              
					try {
						Utskrift.write("NetworkTest/NetworkTest-WaveHeight-" + k + "-Lon-" + i + "-Lat-" + j + ".txt", WTC.fakeWaveTest(i, i + 5 , j, j + 5 , k/14.0));
					} catch (IOException ex) {
						Logger.getLogger(TestOfNetwork.class.getName()).log(Level.SEVERE, null, ex);
					}
					k += waveStep;
				}
				j+=stepLength;
			}
			i+=stepLength;
		}

	}
}
