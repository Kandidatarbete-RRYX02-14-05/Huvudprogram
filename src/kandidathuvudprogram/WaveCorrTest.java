package kandidathuvudprogram;
import java.awt.Point;
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
	String[] datum;		// StartDatum är det datum då datumföljden med träningsdata startar
	int[] nbrOfHiddenNeurons;//nbrOfHiddenNeurons säger hur många lager och hur många neuroner varje dolt lager har.
	int resetParameter;		// Nätverket resetar om det inte blir förbättras mer än 1% på resetParameter iterationer. resetParameter = stänger av den funktionen.
	boolean threshold;		// Är om nätverket ska jobba med threshold
	public enum TrainingFunction{activationsigmoid, activationtanh}; //vilken av dessa träningsfunktioner som ska användas
	MLTrain train;			
	BufferedMLDataSet buffSet;
	BasicNetwork network;
	public enum TrainingType{resilientpropagation}	//använda resilientpropagation (enda alternativet)
	Import imp;
	String datumFilPath;
	int inputSize; //antalet datapunkter i vågdatan 
	int idealSize; //antalet datapunkter i gravimeterdatan

	// KONSTRUKTORER


	public WaveCorrTest(){

		alpha = 0.99;
		dividerWave = 0;
		dividerGrav = 15;
		window = "rectangular";
		nbrOfHiddenNeurons = new int[] {150};
		resetParameter = 0;
		threshold = false;
		imp = new Import();
		datumFilPath = "datumfil.txt";
		datum = GetDataHgsChalmers.generateDateString("2014-01-06", "2014-01-06");

		File tmpFile = new File("Data/Network/trainingData.bin");
		if((tmpFile.exists() == false))
			Filemanager.createBin(datum ,"trainingData", alpha, window, dividerWave, dividerGrav);



		// skapar en "BufferedReader" från .bin-filen
		buffSet = new BufferedMLDataSet(new File("Data/Network/trainingData.bin"));

		inputSize = buffSet.getInputSize(); // sätter storlekar på in och utdata som kan användas i olika tillämpningar
		idealSize = buffSet.getIdealSize(); //

		// Skapar nätverket	
		network = BuildNetwork(nbrOfHiddenNeurons, threshold, "activationSigmoid");

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
		this.nbrOfHiddenNeurons = nbrOfHiddenNeurons;
		resetParameter = 0;
		threshold = false;
		imp = new Import();
		datumFilPath = "indatumfil.txt";
		datum = imp.importWhole(datumFilPath);

		File tmpFile = new File("Data/Network/trainingData.bin");
		if((tmpFile.exists() == false))
			Filemanager.createBin(datum ,"trainingData", alpha, window, dividerWave, dividerGrav);

		// skapar en "BufferedReader" från .bin-filen
		buffSet = new BufferedMLDataSet(new File("Data/Network/trainingData.bin"));

		inputSize = buffSet.getInputSize(); // sätter storlekar på in och utdata som kan användas i olika tillämpningar
		idealSize = buffSet.getIdealSize(); //

		// Skapar nätverket	
		network = BuildNetwork(nbrOfHiddenNeurons, threshold, "ActivationSigmoid");

		// train the neural network
		train = new ResilientPropagation(network, buffSet,0.01,0.01);

		if(resetParameter != 0){ // 'resetParameter' = ger att den aldrig börjar om
			train.addStrategy(new RequiredImprovementStrategy(1000)); // reset if improve is less than 1% over 'resetParameter' cycles
		}

	}

	public WaveCorrTest(String datumFilPath, int[] nbrOfHiddenNeurons, boolean threshold, double alpha, 
			String window, double dividerWave, double dividerGrav, int resetParameter, String trainStr, String functionStr ){

		this.dividerWave = dividerWave;
		this.dividerGrav = dividerGrav;
		this.nbrOfHiddenNeurons = nbrOfHiddenNeurons;
		this.threshold = threshold;
		this.alpha = alpha;
		this.window = window;
		this.resetParameter = resetParameter;
		imp = new Import();	
		this.datumFilPath = datumFilPath;
		datum = imp.importWhole(datumFilPath);

		File tmpFile = new File("Data/Network/trainingData.bin");
		if((tmpFile.exists() == false))
			Filemanager.createBin(datum ,"trainingData", alpha, window, dividerWave, dividerGrav); 

		// skapar en "BufferedReader" från .bin-filen
		buffSet = new BufferedMLDataSet(new File("Data/Network/trainingData.bin"));

		inputSize = buffSet.getInputSize(); // sätter storlekar på in och utdata som kan användas i olika tillämpningar
		idealSize = buffSet.getIdealSize(); //

		// Skapar nätverket	
		network = BuildNetwork(nbrOfHiddenNeurons, threshold, functionStr);

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

	public BasicNetwork BuildNetwork(int[] nbrOfHiddenNeurons, boolean threshold, String functionStr){


		TrainingFunction fun = TrainingFunction.valueOf(functionStr.toLowerCase());


		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, false, inputSize));

		for(int i = 0; i < nbrOfHiddenNeurons.length; i++){
			switch (fun){
			case activationsigmoid:
				network.addLayer(new BasicLayer(new ActivationSigmoid(), threshold, nbrOfHiddenNeurons[i]));
				break;
			case activationtanh:
				network.addLayer(new BasicLayer(new ActivationTANH(), threshold, nbrOfHiddenNeurons[i]));
				break;
			default: 
				throw new IllegalArgumentException("Error: illegal activationFunction");	
			}

		}

		network.addLayer(new BasicLayer(new ActivationSigmoid(), threshold, idealSize));
		network.getStructure().finalizeStructure();
		network.reset();
		return network;
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

	public BufferedMLDataSet networkGenErrorLoad (){

		File tmpFile = new File("Data/Network/genErrorData.bin");
		if((tmpFile.exists() == false)){
			String[] tmpDatum = imp.importWhole("genErrorDatumFil.txt");	
			Filemanager.createBin(tmpDatum, "genErrorData", alpha, window, dividerWave, dividerGrav);
		}

		BufferedMLDataSet buffGenSet = new BufferedMLDataSet(new File("Data/Network/genErrorData.bin"));
		return buffGenSet;
	}

	public double networkGenErrorTest (BufferedMLDataSet set){
		return network.calculateError(set);
	}

	public void updateBin(){
		String[] tmpDatum = imp.importWhole("genErrorDatumFil.txt");	
		Filemanager.createBin(datum, "trainingData", alpha, window, dividerWave, dividerGrav);
		Filemanager.createBin(tmpDatum, "genErrorData", alpha, window, dividerWave, dividerGrav);
	}

	public double[] fakeWaveTest(int[] nbrsToTry, double waveHeight){ 

		double[] tmp = new double[inputSize];

		for ( int i = 0; i < nbrsToTry.length ; i++){
			if (nbrsToTry[i] >= inputSize || nbrsToTry[i] < 0){
				throw new IllegalArgumentException("All the numbers in the input arry to fakeWaveTest need to be within [0, idealSize-1] ; nbr " + i + " is not. :(");
			}
			else{
				tmp[nbrsToTry[i]] = waveHeight; 
			}
		}
		BasicMLData tmpML = new BasicMLData(tmp); 
		return network.compute(tmpML).getData();
	}

	public double[] fakeWaveTest(int X1, int X2, int Y1, int Y2, double waveHeight){

		Point[] testPoints = new Point[Math.abs((X2-X1)*(Y2-Y1))];

		for (int i = 0; i < Math.abs(X2-X1); i++){
			for (int j = 0; j < Math.abs(Y2-Y1); j++){
				testPoints[i*Math.abs(Y2-Y1) +j] = new Point(X1+i, Y1+j);
			}
		}
		return fakeWaveTest(Filemanager.choosePoints(testPoints),waveHeight);
	}
}
