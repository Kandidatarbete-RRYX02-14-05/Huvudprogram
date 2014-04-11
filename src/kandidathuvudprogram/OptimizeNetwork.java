package kandidathuvudprogram;

import java.io.IOException;


/**
 *
 * @author marpol
 *
 */
public class OptimizeNetwork {

	WaveCorrTest network;

	public static void main(String args[]){
		OptimizeNetwork optNet = new OptimizeNetwork(); 
		optNet.numberNeuronTest(150,160);
	}
	
	
	public OptimizeNetwork(){

	}

	/**
	 *
	 * @param maxError
	 * @param maxIteration
	 * @param frequancyGenCorr
	 * @return minimum of genError
	 */
	private double minimizeGenError(double maxError, int maxIteration, int frequancyGenCorr){
		double[] error = new double[maxIteration];
		double[] genError = new double[maxIteration/frequancyGenCorr];

		int step = -1;

		do {
			step++;
			error[step] = network.networkTrain();

			if ( (step % frequancyGenCorr) == 0){
				genError[step] = network.networkGenErrorTest("2014-01-06","00");
			}
		} while (error[step] > maxError && step < maxIteration);

		return getMin(genError)[1]; // returns minimum of genError
	}


	private void numberNeuronTest(int minNeurons, int maxNeurons){

		int[] neuronNo = new int[maxNeurons - minNeurons + 1];
		for (int i = 0; i < (maxNeurons-minNeurons); i++){
			neuronNo[i] = minNeurons + i;
		}
		double[] neuronError = new double[neuronNo.length];

		for (int i=0; i<neuronNo.length; i++){
			network = new WaveCorrTest(new int[]  {neuronNo[i]});
			neuronError[i] = minimizeGenError(0,100,1);
		}

		try {
			Utskrift.write("Data/Matlabfiler/neuronErrorTest.txt", neuronError);
		} catch (IOException e) {

		}
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