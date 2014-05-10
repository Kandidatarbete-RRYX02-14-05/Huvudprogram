/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kandidathuvudprogram;

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
	TestOfNetwork test = new TestOfNetwork(10,100);
	test.testOne();
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

    // lat 1,35
    // lon -70,15
    public void testOne() { //nyttnamn

        int latStart = 35,
                latStop = 75,
                lonStart = -70,
                lonStop = 15;
        int stepLength = 5;
        
        double waveMin = 1;
        double waveMax = 25;
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
                        Utskrift.write("NetworkTest/NetworkTest-WaveHeight-" + k + "-Lon-" + i + "-Lat-" + j + ".txt", WTC.fakeWaveTest(i * 10, (i + 1) * 10, j * 10, (j + 1) * 10, k));
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
