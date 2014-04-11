package kandidathuvudprogram;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
public class WaveCorrTest {

	double alpha;
	String window, startDatum, slutDatum;
	int[] nbrOfHiddenNeurons;
	int resetParameter;
	boolean threshold;
	MLTrain train;
	BufferedMLDataSet buffSet;
	BasicNetwork network;
	
	// KONSTRUKTORER
	
	public WaveCorrTest(){

		alpha = 0.99;
		window = "rectangular";
		startDatum = "2014-01-06";
		slutDatum = "2014-01-06";
		nbrOfHiddenNeurons = new int[] {150};
		resetParameter = 0;
		threshold = false;


		String[] dates = GetDataHgsChalmers.generateDateString(startDatum, slutDatum);
		Filemanager.createBin(dates, alpha, window,0,15);

		// skapar en "BufferedReader" från .bin-filen
		buffSet = new BufferedMLDataSet(new File("Data/Network/trainingData.bin"));

		// Skapar nätverket	
		network = BuildNetwork(buffSet.getInputSize(), buffSet.getIdealSize() , nbrOfHiddenNeurons, threshold);

		// train the neural network
		train = new ResilientPropagation(network, buffSet);

		if(resetParameter != 0){ // 'resetParameter' = ger att den aldrig börjar om
			train.addStrategy(new RequiredImprovementStrategy(1000)); // reset if improve is less than 1% over 'resetParameter' cycles
		}

	}
	
	public WaveCorrTest(int[] nbrOfHiddenNeurons){

		this.alpha = 0.99;
		window = "rectangular";
		startDatum = "2014-01-06";
		slutDatum = "2014-01-06";
		this.nbrOfHiddenNeurons = nbrOfHiddenNeurons;
		resetParameter = 0;
		threshold = false;


		String[] dates = new String[]{startDatum}; //GetDataHgsChalmers.generateDateString(startDatum, slutDatum);
		Filemanager.createBin(dates, alpha, window,0,15);

		// skapar en "BufferedReader" från .bin-filen
		buffSet = new BufferedMLDataSet(new File("Data/Network/trainingData.bin"));

		// Skapar nätverket	
		network = BuildNetwork(buffSet.getInputSize(), buffSet.getIdealSize() , nbrOfHiddenNeurons, threshold);

		// train the neural network
		train = new ResilientPropagation(network, buffSet);

		if(resetParameter != 0){ // 'resetParameter' = ger att den aldrig börjar om
			train.addStrategy(new RequiredImprovementStrategy(1000)); // reset if improve is less than 1% over 'resetParameter' cycles
		}

	}
	
	public WaveCorrTest(String startDatum, String slutDatum, int[] nbrOfHiddenNeurons, boolean threshold, double alpha, String window, int resetParameter, String trainStr ){

		this.startDatum = startDatum;
		this.slutDatum = slutDatum;
		this.nbrOfHiddenNeurons = nbrOfHiddenNeurons;
		this.threshold = threshold;
		this.alpha = alpha;
		this.window = window;
		this.resetParameter = resetParameter;
		

		String[] dates = GetDataHgsChalmers.generateDateString(startDatum, slutDatum);
		Filemanager.createBin(dates, alpha, window,0,15); //VARNING!! HÄR KAN DET BLI JOBBIGT OM GRAVIMETERDATAN ÄR STÖRRE ÄN 15!! 

		// skapar en "BufferedReader" från .bin-filen
		buffSet = new BufferedMLDataSet(new File("Data/Network/trainingData.bin"));

		// Skapar nätverket	
		network = BuildNetwork(buffSet.getInputSize(), buffSet.getIdealSize() , nbrOfHiddenNeurons, threshold);

		// Sätter 'train' till vald metod
		switch (trainStr.toLowerCase()){
		case "resilientpropagation":
			train = new ResilientPropagation(network, buffSet);
			break;
		default: 
			train = new ResilientPropagation(network, buffSet);
			break; 
		}
			

		if(resetParameter != 0){ // 'resetParameter' = ger att den aldrig börjar om
			train.addStrategy(new RequiredImprovementStrategy(1000)); // reset if improve is less than 1% over 'resetParameter' cycles
		}

	}
	
	
	// METODER

	public BasicNetwork BuildNetwork(int inputSize, int idealSize, int[] nbrOfHiddenNeurons, boolean threshold){
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, false, inputSize));
		
		for(int i = 0; i < nbrOfHiddenNeurons.length; i++){
		network.addLayer(new BasicLayer(new ActivationSigmoid(), threshold, nbrOfHiddenNeurons[i]));
		}
		
		network.addLayer(new BasicLayer(new ActivationSigmoid(), threshold, idealSize));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
	}

	public void printInfo(BufferedMLDataSet buffSet, BasicNetwork network) {
		// test the neural network
		System.out.println("Neural Network Results:");
		for(MLDataPair pair: buffSet ) {
			final MLData output = network.compute(pair.getInput()); // network.compute(MlData) använder nätverket

			System.out.println();
			System.out.println();
			for (int i = 0;i<5;i++){
				System.out.println("ideal=" + pair.getIdeal().getData(i) + ", actual=" + output.getData(i)); // instanceOfMldata.getData(index i) ger utdata nbr i.
			}
			Chart.useRelevantChart(network.compute(pair.getInput()).getData(), "NetOutPlot", alpha, window, 16384);
		}
	}
	
	public double networkTrain(){
		train.iteration();
		return train.getError();
	}
	
	public double networkGenErrorTest (String datum, String tid){
		double[][] tmpWave = Filemanager.readSingleWaveFile(datum, tid, 0);
		double[][] tmpGrav = Filemanager.readGravFileInParts(datum);
		double[][] tmp1Grav = new double[1][tmpGrav[0].length];
		tmp1Grav[0] = tmpGrav[0];
		
		BasicMLDataSet set = new BasicMLDataSet(tmpWave, tmp1Grav);
		
		MLTrain tmpTrain = new ResilientPropagation(network, set);
		
		
		return tmpTrain.getError();
	}

}
