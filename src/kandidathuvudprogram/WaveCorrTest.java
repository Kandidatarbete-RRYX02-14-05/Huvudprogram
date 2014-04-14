package kandidathuvudprogram;
import java.io.File;

import kandidathuvudprogram.Window.Windows;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
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

	
	double alpha;			// alpha är ett tal som bestämmer hur Powerspectrumet ska göras. Mer specifikt har det med filtret att göra.
	double dividerWave;		// divierWave är hur mycket vi skalar ned vågdatan med. 0 ger ett defaultvärde på 21.2;
	double dividerGrav;     // divierGrav är hur mycket vi skalar ned gravimeterdatan med. 0 ger ett defaultvärde på 13;
	String window;			// window är vilket fönster vi använder till vårat PowerSpectrum;
	String startDatum;		// StartDatum är det datum då datumföljden med träningsdata startar
	String slutDatum;		// SlutDatum är det datum då datumföljden med träningsdata slutar
	int[] nbrOfHiddenNeurons;//nbrOfHiddenNeurons säger hur många lager och hur många neuroner varje dolt lager har.
	int resetParameter;		// Nätverket resetar om det inte blir förbättras mer än 1% på resetParameter iterationer. resetParameter = stänger av den funktionen.
	boolean threshold;		// Är om nätverket ska jobba med threshold
	MLTrain train;			
	BufferedMLDataSet buffSet;
	BasicNetwork network;
	public enum TrainingType{resilientpropagation}
	

	// KONSTRUKTORER

	
	public WaveCorrTest(){

		alpha = 0.99;
		dividerWave = 0;
		dividerGrav = 15;
		window = "rectangular";
		startDatum = "2014-01-06";
		slutDatum = "2014-01-06";
		nbrOfHiddenNeurons = new int[] {150};
		resetParameter = 0;
		threshold = false;


		String[] dates = GetDataHgsChalmers.generateDateString(startDatum, slutDatum);
		Filemanager.createBin(dates, alpha, window, dividerWave, dividerGrav);

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
		dividerWave = 0;
		dividerGrav = 15;
		window = "rectangular";
		startDatum = "2014-01-06";
		slutDatum = "2014-01-06";
		this.nbrOfHiddenNeurons = nbrOfHiddenNeurons;
		resetParameter = 0;
		threshold = false;


		String[] dates = new String[]{startDatum}; //GetDataHgsChalmers.generateDateString(startDatum, slutDatum);
		Filemanager.createBin(dates, alpha, window, dividerWave, dividerGrav);

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

	public WaveCorrTest(String startDatum, String slutDatum, int[] nbrOfHiddenNeurons, boolean threshold, double alpha, 
		   String window, double dividerWave, double dividerGrav, int resetParameter, String trainStr ){
		
		this.dividerWave = dividerWave;
		this.dividerGrav = dividerGrav;
		this.startDatum = startDatum;
		this.slutDatum = slutDatum;
		this.nbrOfHiddenNeurons = nbrOfHiddenNeurons;
		this.threshold = threshold;
		this.alpha = alpha;
		this.window = window;
		this.resetParameter = resetParameter;
		


		String[] dates = GetDataHgsChalmers.generateDateString(startDatum, slutDatum);
		Filemanager.createBin(dates, alpha, window, dividerWave, dividerGrav); //VARNING!! HÄR KAN DET BLI JOBBIGT OM GRAVIMETERDATAN ÄR STÖRRE ÄN 15!! 

		// skapar en "BufferedReader" från .bin-filen
		buffSet = new BufferedMLDataSet(new File("Data/Network/trainingData.bin"));

		// Skapar nätverket	
		network = BuildNetwork(buffSet.getInputSize(), buffSet.getIdealSize() , nbrOfHiddenNeurons, threshold);

		// Sätter 'train' till vald metod
		TrainingType type = TrainingType.valueOf(trainStr.toLowerCase());
		switch (type){
		case resilientpropagation:
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

	public double networkMultiTrain(int m){
		train.iteration(m);
		return train.getError();
	}

	/**
	 *  Används innan man kör networkGenErrorTest för att skapa tmpTrain vilket är datan man testar mot.
	 * @param datum tex "2014-01-06"
	 * @param tid tex "06"
	 * @return
	 */
	public BasicMLDataSet networkGenErrorLoad (String datum, String tid){ 
		double[][] tmpWave = Filemanager.readWaveFile(datum.substring(2), dividerWave);
		double[][] tmp1Wave = new double[1][tmpWave[0].length];
		tmp1Wave[0] = tmpWave[(int) (Double.parseDouble(tid)+1)/6];
		
		double[][] tmpGrav = Filemanager.readGravFileInParts(datum.substring(2));
		PowerSpectrum spectrum = new PowerSpectrum(tmpGrav[(int) (Double.parseDouble(tid)+1)/6], alpha, window, 4);
		double[][] tmp1Grav = new double[1][spectrum.getSpectrum().length];
		tmp1Grav[0] = spectrum.getRelevantSpectrum(dividerGrav); // +1 för att slippa avrundningsfel

		
		
		BasicMLDataSet set = new BasicMLDataSet(tmp1Wave, tmp1Grav);

		return set;
	}

	public double networkGenErrorTest (BasicMLDataSet set){
		return network.calculateError(set);
	}
}
