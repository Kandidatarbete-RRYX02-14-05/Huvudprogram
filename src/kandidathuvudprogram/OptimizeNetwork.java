package kandidathuvudprogram;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.ml.train.MLTrain;


/**
 *
 * @author marpol
 *
 */
public class OptimizeNetwork {

	WaveCorrTest network;

	public static void main(String args[]){
		OptimizeNetwork optNet = new OptimizeNetwork(); 
		optNet.numberNeuronTest(140, 160, 50, 0.0);
	}
	
	
	public OptimizeNetwork(){

	}

	/**
         * Mycket trevlig funktion;)
         * @param maxError
         * @param maxIteration
         * @param frequancyGenCorr
         * @param nrNeurons
         * @return minimum of genError
         */
	private double minimizeGenError(double maxError, int maxIteration, int frequancyGenCorr, int nrNeurons){
		double[] error = new double[maxIteration+1];
		double[] genError = new double[maxIteration/frequancyGenCorr+1];
                
		BufferedMLDataSet testSet = network.networkGenErrorLoad(); // slumpa dag här!?

		int step = -1;

		do {
			step++;
			error[step] = network.networkTrain();
			if ( (step % frequancyGenCorr) == 0){
				genError[step] = network.networkGenErrorTest(testSet);
			}
		} while (error[step] > maxError && step < maxIteration);
                
            try {    // Writes the error for each iteration            
                Utskrift.write("Data/Matlabfiler/NeuronConvergence/NeuronTrainError-" + nrNeurons + ".txt" , error);
                Utskrift.write("Data/Matlabfiler/NeuronConvergence/NeuronGenError-" + nrNeurons + ".txt" , genError);
            } catch (IOException ex) {
                Logger.getLogger(OptimizeNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }

		return getMin(genError)[1]; // returns minimum of genError
	}


        private void numberNeuronTest(int minNeurons, int maxNeurons, int maxIteration, double maxErrorTrain) {

            int[] neuronNo = new int[maxNeurons - minNeurons + 1];
            for (int i = 0; i < (maxNeurons - minNeurons + 1); i++) {
                neuronNo[i] = minNeurons + i;
            }
            double[] neuronError = new double[neuronNo.length];

            for (int i = 0; i < neuronNo.length; i++) {
                network = new WaveCorrTest(new int[]{neuronNo[i]});
                neuronError[i] = minimizeGenError(maxErrorTrain, maxIteration, 1, neuronNo[i]);
                System.out.println("Antal neuroner: " + neuronNo[i]);
            }

            try { // skriver ut genError vs nbrNeurons
                Utskrift.write("Data/Matlabfiler/neuronErrorTest-" + neuronNo[0] + "-" + neuronNo[neuronNo.length-1] + ".txt", neuronError);
            } catch (IOException e) {

            }
    }




        private String generateRandomDate(){
            
            return "2014-01-06";
        }
        private String generateRandomTime(){
            double rand = Math.random();
            
            if (rand < 0.25)
                return "00";
            else if (rand >= 0.25 && rand < 0.5)
                return "06";
            else if (rand >= 0.5 && rand < 0.75)
                return "12";    
            else return "18";
        }

	/**
	 * gets minimum value of array
	 * @param array
	 * @return a double array --> array[0] = indexOfSmallest, array[1] = smallestValue.
	 */
	private double[] getMin(double[] array){
		double smallest = array[0];
		int smallestIndex = 0;

		for (int i = 1; i < array.length; i++){
			if (array[i] < smallest){
				smallest = array[i];
				smallestIndex = i;
			}
		}

		return new double[]{ (double) smallestIndex, smallest };
	}
}
