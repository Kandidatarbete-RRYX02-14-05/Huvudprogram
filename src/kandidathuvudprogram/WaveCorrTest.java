package kandidathuvudprogram;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import kandidathuvudprogram.Window.Windows;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;
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
	String datumFil="TrainDatum2.txt";

	// KONSTRUKTORERf


	public WaveCorrTest(){

		alpha = 0.99;
		dividerWave = 0;
		dividerGrav = 15;
		window = "rectangular";
		nbrOfHiddenNeurons = new int[] {150};
		resetParameter = 0;
		threshold = false;
		imp = new Import();
		datumFilPath = datumFil;
		datum = GetDataHgsChalmers.generateDateString("2014-01-06", "2014-01-06");

		File tmpFile = new File("Data/Network/trainingData.bin");
		if((tmpFile.exists() == false))
			try {
				Filemanager.createBin(datum ,"trainingData", alpha, window, dividerWave, dividerGrav);
			} catch (IOException e) {
				e.printStackTrace();
			}



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

	public WaveCorrTest(int networkNbr){

		alpha = 0.99;
		dividerWave = 0;
		dividerGrav = 15;
		window = "rectangular";
		resetParameter = 0;
		threshold = false;
		imp = new Import();
		datumFilPath = datumFil;
		datum = GetDataHgsChalmers.generateDateString("2014-01-06", "2014-01-06");

		File tmpFile = new File("Data/Network/trainingData.bin");
		if((tmpFile.exists() == false))
			try {
				Filemanager.createBin(datum ,"trainingData", alpha, window, dividerWave, dividerGrav);
			} catch (IOException e) {
				e.printStackTrace();
			}



		// skapar en "BufferedReader" från .bin-filen
		buffSet = new BufferedMLDataSet(new File("Data/Network/trainingData.bin"));

		inputSize = buffSet.getInputSize(); // sätter storlekar på in och utdata som kan användas i olika tillämpningar
		idealSize = buffSet.getIdealSize(); //

		// Skapar nätverket	
		// network = BuildNetwork(nbrOfHiddenNeurons, threshold, "activationSigmoid");
		network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File("Data/Network/savedNetwork" + networkNbr));
		nbrOfHiddenNeurons = new int[network.getLayerCount()-2];
		for (int i = 0; i <nbrOfHiddenNeurons.length; i++){
			nbrOfHiddenNeurons[i] = network.getLayerNeuronCount(i);
		}

		// train the neural network
		train = new ResilientPropagation(network, buffSet);

		if(resetParameter != 0){ // 'resetParameter' = ger att den aldrig börjar om
			train.addStrategy(new RequiredImprovementStrategy(1000)); // reset if improve is less than 1% over 'resetParameter' cycles
		}

	}

	public WaveCorrTest(File loadFile){

		alpha = 0.99;
		dividerWave = 0;
		dividerGrav = 15;
		window = "rectangular";
		resetParameter = 0;
		threshold = false;
		imp = new Import();
		datumFilPath = datumFil;
		datum = GetDataHgsChalmers.generateDateString("2014-01-06", "2014-01-06");

		File tmpFile = new File("Data/Network/trainingData.bin");
		if((tmpFile.exists() == false))
			try {
				Filemanager.createBin(datum ,"trainingData", alpha, window, dividerWave, dividerGrav);
			} catch (IOException e) {
				e.printStackTrace();
			}



		// skapar en "BufferedReader" från .bin-filen
		buffSet = new BufferedMLDataSet(new File("Data/Network/trainingData.bin"));

		inputSize = buffSet.getInputSize(); // sätter storlekar på in och utdata som kan användas i olika tillämpningar
		idealSize = buffSet.getIdealSize(); //

		// Skapar nätverket	
		// network = BuildNetwork(nbrOfHiddenNeurons, threshold, "activationSigmoid");
		network = (BasicNetwork) EncogDirectoryPersistence.loadObject(loadFile);
		nbrOfHiddenNeurons = new int[network.getLayerCount()-2];
		for (int i = 0; i <nbrOfHiddenNeurons.length; i++){
			nbrOfHiddenNeurons[i] = network.getLayerNeuronCount(i);
		}

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
		threshold = true;
		imp = new Import();
		datumFilPath = datumFil;
		datum = imp.importWhole(datumFilPath);
		for(int i=0; i<datum.length; i++){
			datum[i]=datum[i].replace("\n", "").replace("\r", "");
		}
		File tmpFile = new File("Data/Network/trainingData.bin");
		if((tmpFile.exists() == false))
			try {
				Filemanager.createBin(datum ,"trainingData", alpha, window, dividerWave, dividerGrav);
			} catch (IOException e) {
				e.printStackTrace();
			}

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

	public WaveCorrTest(String datumFilPath, String genDatumFilPath, int[] nbrOfHiddenNeurons, boolean threshold, double alpha, 
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
		for(int i=0; i<datum.length; i++){
			datum[i]=datum[i].replace("\n", "").replace("\r", "");
		}

		File tmpFile = new File("Data/Network/trainingData.bin");
		if((tmpFile.exists() == false))
			try {
				Filemanager.createBin(datum ,"trainingData", alpha, window, dividerWave, dividerGrav);
			} catch (IOException e) {
				e.printStackTrace();
			} 

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
			String[] tmpDatum = imp.importWhole("genDatum1.txt");	
			try {
				Filemanager.createBin(tmpDatum, "genErrorData", alpha, window, dividerWave, dividerGrav);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BufferedMLDataSet buffGenSet = new BufferedMLDataSet(new File("Data/Network/genErrorData.bin"));
		return buffGenSet;
	}


	public BufferedMLDataSet networkGenErrorLoad (String genDates){

		File tmpFile = new File("Data/Network/genErrorData.bin");
		if((tmpFile.exists() == false)){
			String[] tmpDatum = imp.importWhole(genDates);	
			try {
				Filemanager.createBin(tmpDatum, "genErrorData", alpha, window, dividerWave, dividerGrav);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BufferedMLDataSet buffGenSet = new BufferedMLDataSet(new File("Data/Network/genErrorData.bin"));
		return buffGenSet;
	}
	public double networkGenErrorTest (BufferedMLDataSet set){
		return network.calculateError(set);
	}

	public double[] fakeWaveTest(int[] nbrsToTry, double waveHeight){ 

		double[] tmp = new double[inputSize];
		String[] lugnDag = new String[inputSize];
		Import imp = new Import();
		lugnDag=imp.importWhole("wavedata/removedmissing/20100726_12.tsv");
		for ( int i=0; i<inputSize; i++){
			tmp[i]=Math.pow(Double.parseDouble(lugnDag[i])/14.0,2);
		}

		for ( int i = 0; i < nbrsToTry.length ; i++){
			if (nbrsToTry[i] >= inputSize || nbrsToTry[i] < 0){
				throw new IllegalArgumentException("All the numbers in the input arry to fakeWaveTest need to be within [0, idealSize-1] ; nbr " + i + " is not. :(");
			}
			else{
				tmp[nbrsToTry[i]] = Math.pow(waveHeight/14,2); 
			}
		}
		BasicMLData tmpML = new BasicMLData(tmp); 
		return network.compute(tmpML).getData();
	}

	// Do not even try to understand.. It is master hardcode.. 
	public double[] pyramidTest(Point p){ 

		double[] tmp = new double[inputSize];
		String[] lugnDag = new String[inputSize];
		Import imp = new Import();
		lugnDag=imp.importWhole("wavedata/removedmissing/20100726_12.tsv");
		
		for ( int i=0; i<inputSize; i++){
			tmp[i]=Double.parseDouble(lugnDag[i])/14.0;
		}
		try {
			Utskrift.write("NetworkTest/NetworkTest-" + "lugndag" + "-waveH-" + 0 + ".txt",tmp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Point[] pArr = {p};
		

		if (Filemanager.choosePoints(pArr).length<1){
			
			double[] A = {0, 0}; 
			return A;
		
		} else {
			
			 tmp[Filemanager.choosePoints(pArr)[0]] = Math.pow(14/14.0,2);
			
		Point[] pArr2 = {new Point((int) (p.getX() + 1.5) ,(int) (p.getY()+1.5)), 
				new Point((int) (p.getX() + 0.5) ,(int) (p.getY()+1.5)),
				new Point((int) (p.getX() - 0.5) ,(int) (p.getY()+1.5)),
				new Point((int) (p.getX() + 1.5) ,(int) (p.getY()+0.5)),
				new Point((int) (p.getX() - 0.5) ,(int) (p.getY()+0.5)),
				new Point((int) (p.getX() + 1.5) ,(int) (p.getY()-0.5)),
				new Point((int) (p.getX() + 0.5) ,(int) (p.getY()-0.5)),
				new Point((int) (p.getX() - 0.5) ,(int) (p.getY()-0.5))};


		 for (int i = 0; i<Filemanager.choosePoints(pArr2).length; i++){
				tmp[Filemanager.choosePoints(pArr2)[i]] = Math.pow(10.0/14,2);
			 }
			 
		 /*
		
		Point[] pArr3 = {new Point((int) (p.getX() + 2.5) ,(int) (p.getY()+2.5)), 
				new Point((int) (p.getX() + 1.5) ,(int) (p.getY()+2.5)),
				new Point((int) (p.getX() + 0.5) ,(int) (p.getY()+2.5)),
				new Point((int) (p.getX() - 0.5) ,(int) (p.getY()+2.5)),
				new Point((int) (p.getX() -1.5) ,(int) (p.getY()+2.5)),
				new Point((int) (p.getX() + 2.5) ,(int) (p.getY()+1.5)),
				new Point((int) (p.getX() - 1.5) ,(int) (p.getY()+1.5)),
				new Point((int) (p.getX() + 2.5) ,(int) (p.getY()+0.5)),
				new Point((int) (p.getX() - 1.5) ,(int) (p.getY()+0.5)),
				new Point((int) (p.getX() + 2.5) ,(int) (p.getY()-0.5)),
				new Point((int) (p.getX() - 1.5) ,(int) (p.getY()-0.5)),
				new Point((int) (p.getX() +2.5) ,(int) (p.getY() -1.5)),
				new Point((int) (p.getX() + 1.5) ,(int) (p.getY() - 1.5)),
				new Point((int) (p.getX() + 0.5) ,(int) (p.getY()-1.5)),
				new Point((int) (p.getX() - 0.5) ,(int) (p.getY()-1.5)),
				new Point((int) (p.getX() - 1.5) ,(int) (p.getY()-1.5))};

		
		for (int i = 0; i<Filemanager.choosePoints(pArr3).length; i++){

			tmp[Filemanager.choosePoints(pArr3)[i]] = Math.pow(7.0/14,2);
		}
		
		Point[] pArr4 = {new Point((int) (p.getX() + 3.5) ,(int) (p.getY()+3.5)), 
				new Point((int) (p.getX() + 2.5) ,(int) (p.getY()+3.5)),
				new Point((int) (p.getX() + 1.5) ,(int) (p.getY()+3.5)),
				new Point((int) (p.getX() + 0.5) ,(int) (p.getY()+3.5)),
				new Point((int) (p.getX() - 0.5) ,(int) (p.getY()+3.5)),
				new Point((int) (p.getX() - 1.5) ,(int) (p.getY()+3.5)),
				new Point((int) (p.getX() - 2.5) ,(int) (p.getY()+3.5)),
				new Point((int) (p.getX() + 3.5) ,(int) (p.getY()+2.5)),
				new Point((int) (p.getX() - 2.5) ,(int) (p.getY()+2.5)),
				new Point((int) (p.getX() + 3.5) ,(int) (p.getY()+1.5)),
				new Point((int) (p.getX() - 2.5) ,(int) (p.getY()+1.5)),
				new Point((int) (p.getX() + 3.5) ,(int) (p.getY()+0.5)),
				new Point((int) (p.getX() - 2.5) ,(int) (p.getY()+0.5)),
				new Point((int) (p.getX() + 3.5) ,(int) (p.getY()-0.5)),
				new Point((int) (p.getX() - 2.5) ,(int) (p.getY()-0.5)),
				new Point((int) (p.getX() + 3.5) ,(int) (p.getY()-1.5)),
				new Point((int) (p.getX() - 2.5) ,(int) (p.getY()-1.5)),
				new Point((int) (p.getX() + 3.5) ,(int) (p.getY()-2.5)),
				new Point((int) (p.getX() + 2.5) ,(int) (p.getY()-2.5)),
				new Point((int) (p.getX() + 1.5) ,(int) (p.getY()-2.5)),
				new Point((int) (p.getX() + 0.5) ,(int) (p.getY()-2.5)),
				new Point((int) (p.getX() - 0.5) ,(int) (p.getY()-2.5)),
				new Point((int) (p.getX() - 1.5) ,(int) (p.getY()-2.5)),
				new Point((int) (p.getX() - 2.5) ,(int) (p.getY()-2.5))};
		 
		for (int i = 0; i<Filemanager.choosePoints(pArr4).length; i++){

			tmp[Filemanager.choosePoints(pArr4)[i]] = Math.pow(5.0/14,2);
		}
		  
		
			 */
		 

		
		
		
		  
		 BasicMLData tmpML = new BasicMLData(tmp);
		 double[] tmpData = network.compute(tmpML).getData();
		 double[] returnValue = {0 ,0};
		 for (int i = 0; i<tmpData.length; i++)
			 for (int j = 30; j<70 ; j++){
			  returnValue[0] += tmpData[j];
			 }
		 for (int j = 100; j<200 ; j++){
			  returnValue[1] += tmpData[j];
			 }
		 return returnValue;
		}
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

	public void saveNetwork(){

		int networkNbr = 1;
		while(new File("Data/Network/savedNetwork" + networkNbr).exists()){
			networkNbr++;
		} 
		EncogDirectoryPersistence.saveObject(new File("Data/Network/savedNetwork"+networkNbr), network);      
	}

	public void saveNetwork(File saveFile){
		EncogDirectoryPersistence.saveObject(saveFile, network);
	}
}
