package kandidathuvudprogram;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.ml.train.MLTrain;


/**
 *
 * @author Markus (lite Emilio)
 *
 */
public class OptimizeNetwork {



	protected double iterationTime[], neuronError[]; 
	protected int[] numberNeurons;
	volatile boolean doneNeurons[];
	final int maxIteration;
	final double maxErrorTrain;
	Thread thread[];
	public static void main(String args[]){
		OptimizeNetwork optNet = new OptimizeNetwork(200, 0.0); 
		optNet.setNumberNeurons(50,60, 2);
                //optNet.manualNumberNeurons();
                
		optNet.multiThreadNeuronTest(1, "TrainDates1.txt","genDatum1.txt");  
	}

        public void manualNumberNeurons(){
            numberNeurons = new int[5];
            numberNeurons[0] = 50;
            numberNeurons[1] = 100;
            numberNeurons[2] = 150;
            numberNeurons[3] = 200;
            numberNeurons[4] = 250;
            numberNeurons = new int[1];
            numberNeurons[0] = 200;
        }

	public OptimizeNetwork(int maxIteration, double maxErrorTrain){
		this.maxIteration = maxIteration;
		this.maxErrorTrain = maxErrorTrain;
	}

	/**
	 * Mycket trevlig funktion;)
	 * @param maxError
	 * @param maxIteration
	 * @param frequancyGenCorr
	 * @param nrNeurons
	 * @return minimum of genError
	 */
	private double minimizeGenError(double maxError, int frequancyGenCorr, int nrNeurons, WaveCorrTest network){
		double[] error = new double[maxIteration+1];
		double[] genError = new double[maxIteration/frequancyGenCorr+1];
		BufferedMLDataSet testSet = network.networkGenErrorLoad(); // slumpa dag h�r!?
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


	private double minimizeGenError(double maxError, int frequancyGenCorr, int nrNeurons, WaveCorrTest network, String genDates){
		double[] error = new double[maxIteration+1];
		double[] genError = new double[maxIteration/frequancyGenCorr+1];
		BufferedMLDataSet testSet = network.networkGenErrorLoad(genDates); // slumpa dag h�r!?
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
	
	public void numberNeuronTest(int minNeurons, int maxNeurons) {
		long timeStart, timeStop, totTimeStart, totTimeStop;
		totTimeStart = System.nanoTime();
		WaveCorrTest network;

		iterationTime = new double[maxNeurons - minNeurons + 1];
		int[] neuronNo = new int[maxNeurons - minNeurons + 1];
		for (int i = 0; i < (maxNeurons - minNeurons + 1); i++) {
			neuronNo[i] = minNeurons + i;
		}
		double[] neuronError = new double[neuronNo.length];

		for (int i = 0; i < neuronNo.length; i++) {
			timeStart = System.nanoTime(); 
			network = new WaveCorrTest(new int[]{neuronNo[i]});
			neuronError[i] = minimizeGenError(maxErrorTrain, 1, neuronNo[i], network);
			timeStop = System.nanoTime();
			System.out.println("Antal neuroner: " + neuronNo[i] + ".  Tid för träning: " + (timeStop-timeStart)/1000000000.0 + " Sekunder");
			iterationTime[i] = (timeStop-timeStart)/1000000000.0;
		}
		totTimeStop = System.nanoTime();
		System.out.println("Total tid för körning: " + (totTimeStop-totTimeStart)/1000000000.0 + " Sekunder");

		try { // skriver ut genError vs nbrNeurons
			Utskrift.write("Data/Matlabfiler/neuronErrorTest-" + neuronNo[0] + "-" + neuronNo[neuronNo.length-1] + ".txt", neuronError);
			Utskrift.write("Data/Matlabfiler/neuronErrorTestTime-" + neuronNo[0] + "-" + neuronNo[neuronNo.length-1] + ".txt", iterationTime);
		} catch (IOException e) {

		}
		System.exit(0);
	}

	private synchronized boolean grabDoneNeurons(int i){
		if (!doneNeurons[i]){
			doneNeurons[i] = true;
			return true;
		}
		else return false;
	}

	public void neuronThread(int maxIteration, double maxErrorTrain, String TrainDates, String genDates){
		long timeStart, timeStop;

		WaveCorrTest network;

		boolean finished = false;
		int index = -1;

		while (!finished) {
			index = -1;
			for (int i=0; i<doneNeurons.length; i++){
				if(grabDoneNeurons(i)){
					index = i;
					break;
				}
				else if (index == -1 && i == doneNeurons.length - 1){
					finished = true;
				}
			}
			if (index!=-1){
				timeStart = System.nanoTime(); 
				//network = new WaveCorrTest(new int[]{numberNeurons[index]});
                                network = new WaveCorrTest(TrainDates,genDates, new int[]{numberNeurons[index]}, false,0.99, "rectangular", 0, 15, 0, "resilientpropagation","activationsigmoid");
                                
				neuronError[index] = minimizeGenError(maxErrorTrain, 1, numberNeurons[index], network, genDates);
				timeStop = System.nanoTime();
				System.out.println("Antal neuroner: " + numberNeurons[index] + ".  Tid för träning: " + (timeStop-timeStart)/1000000000.0 + " Sekunder"); //�r detta intressant? Kanske bara 
				//print f�r att visa att det �r klart?
				iterationTime[index] = (timeStop-timeStart)/1000000000.0;
			}      
		}


	}


	/**
	 * Bara att sätta fältet "numberNeurons" med de antal neuroner man vill testa innan denna metod körs!  
	 * @param numberThreads 
	 */
	public void multiThreadNeuronTest(int numberThreads, final String TrainDates,final String genDates){
		long totTimeStart, totTimeStop;
		totTimeStart = System.nanoTime();
		thread = new Thread[numberThreads];
		doneNeurons = new boolean[numberNeurons.length];
		iterationTime = new double[numberNeurons.length];
		neuronError = new double[numberNeurons.length];

		for (int i=0; i<numberThreads; i++){
			thread[i] = new Thread() {
				public void run() {
					neuronThread(maxIteration, maxErrorTrain, TrainDates, genDates);
				}
			};
			thread[i].start();
		}
		for(int i=0; i<numberThreads; i++){
			try {
				thread[i].join();	//väntar på att varje tråd blir klar
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		try { // skriver ut genError vs nbrNeurons
			Utskrift.write("Data/Matlabfiler/neuronErrorTest-" + numberNeurons[0] + "-" + numberNeurons[numberNeurons.length-1] + ".txt", neuronError);
			Utskrift.write("Data/Matlabfiler/neuronErrorTestTime-" + numberNeurons[0] + "-" + numberNeurons[numberNeurons.length-1] + ".txt", iterationTime);
		} catch (IOException e) {

		}
		totTimeStop = System.nanoTime();
		System.out.println("Total tid för körning: " + (totTimeStop-totTimeStart)/1000000000.0 + " Sekunder");
		System.exit(0);	//Detta är nog inte ett helt korrekt sätt att avsluta...
	}


	private void setNumberNeurons(int start, int stop, int steplength){
		numberNeurons = new int[stop - start + 1];
		for (int i=0; i< numberNeurons.length; i++){
			numberNeurons[i] = start + i*steplength;
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

	/**t
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
