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

	
        
        protected double iterationTime[], neuronError[]; 
        protected int[] numberNeurons;
        protected boolean doneNeurons[];
        final int maxIteration;
        final double maxErrorTrain;
        Thread thread[];
        
	public static void main(String args[]){
		OptimizeNetwork optNet = new OptimizeNetwork(50, 0.0); 
                //optNet.numberNeuronTest(1, 2);
                optNet.setNumberNeurons(50, 60);
		optNet.multiThreadNeuronTest(3);  
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
                long timeStart, timeStop;
                
		BufferedMLDataSet testSet = network.networkGenErrorLoad(); // slumpa dag här!?

		int step = -1;

		do {
                    timeStart = System.nanoTime();
                    step++;
                    error[step] = network.networkTrain();
                    if ( (step % frequancyGenCorr) == 0){
            		genError[step] = network.networkGenErrorTest(testSet);
                    }
                    timeStop = System.nanoTime();
                    System.out.println("Iteration " + step + ". Tid för iteration: " + (timeStop-timeStart)/1000000000.0);
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

        
        public void neuronThread(int maxIteration, double maxErrorTrain){
            long timeStart, timeStop, totTimeStart, totTimeStop;
            totTimeStart = System.nanoTime();
            WaveCorrTest network;
                    
            boolean finished = false;
            int index = -1, alive = 0;     
                      
            while (!finished) {
                index = -1;
                for (int i=0; i<doneNeurons.length; i++){
                    if (!doneNeurons[i]){
                        doneNeurons[i] = true;
                        index = i;
                        i = doneNeurons.length;
                    }
                    else if (index == -1 && i == doneNeurons.length - 1){
                        finished = true;
                    }
                }
                if (index!=-1){
                    timeStart = System.nanoTime(); 
                    network = new WaveCorrTest(new int[]{numberNeurons[index]});
                    neuronError[index] = minimizeGenError(maxErrorTrain, 1, numberNeurons[index], network);
                    timeStop = System.nanoTime();
                    System.out.println("Antal neuroner: " + numberNeurons[index] + ".  Tid för träning: " + (timeStop-timeStart)/1000000000.0 + " Sekunder");
                    iterationTime[index] = (timeStop-timeStart)/1000000000.0;
                }      
            }
            totTimeStop = System.nanoTime();
            System.out.println("Total tid för körning: " + (totTimeStop-totTimeStart)/1000000000.0 + " Sekunder");

            
            for (int i=0; i<thread.length; i++){
                if (thread[i].isAlive()){
                    alive++;
                }
            }
            if (alive < 2){
                try { // skriver ut genError vs nbrNeurons
                    Utskrift.write("Data/Matlabfiler/neuronErrorTest-" + numberNeurons[0] + "-" + numberNeurons[numberNeurons.length-1] + ".txt", neuronError);
                    Utskrift.write("Data/Matlabfiler/neuronErrorTestTime-" + numberNeurons[0] + "-" + numberNeurons[numberNeurons.length-1] + ".txt", iterationTime);
                    System.exit(0);
                } catch (IOException e) {

                }
            }
        }
        
        /**
         * Bara att sätta fältet "numberNeurons" med de antal neuroner man vill testa innan denna metod körs!  
         * @param numberThreads 
         */
        public void multiThreadNeuronTest(int numberThreads){
            thread = new Thread[numberThreads];
            doneNeurons = new boolean[numberNeurons.length];
            iterationTime = new double[numberNeurons.length];
            neuronError = new double[numberNeurons.length];
            
            for (int i=0; i<numberThreads; i++){
                thread[i] = new Thread() {
                    public void run() {
                        neuronThread(maxIteration, maxErrorTrain);
                    }
                };
                thread[i].start();
            }
        }
        
        private void setNumberNeurons(int start, int stop){
            numberNeurons = new int[stop - start + 1];
            for (int i=0; i< numberNeurons.length; i++){
                numberNeurons[i] = start + i;
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
