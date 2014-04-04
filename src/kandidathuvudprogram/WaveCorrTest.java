package kandidathuvudprogram;
import java.io.File;
import java.util.Iterator;

import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import
org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.
resilient.ResilientPropagation;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
public class WaveCorrTest {

	
	
	
	public static void main(final String args[]) {
		
		
		
		String[] dates = kandidathuvudprogram.GetDataHgsChalmers.generateDateString("2010-06-11","2010-06-11");


		// Testar Import.Java--------
		String fil = "gravidata/" + dates[0] + ".tsf";
		Import imp = new Import();
		String dataTime[], dataValue[];  // tid vid varje värde samt värde vid varje tid...

		dataTime = imp.importWhole(fil);
		dataValue = new String[dataTime.length];
		String[] temp;
		for (int i=0; i<dataTime.length-1; i++){
			temp = dataTime[i].split(" ");
			dataValue[i] = temp[temp.length-1];
			dataTime[i] = temp[0];
		}
		
		double testdata1[] = new double[dataTime.length-1];
		for (int i=0; i<testdata1.length; i++){
			testdata1[i] = Double.parseDouble(dataValue[i]);
		};
		
		double alpha=0.99;
		String windowName="Hanning";
		PowerSpectrum testPower = new PowerSpectrum(testdata1,alpha,windowName,4);
		
		/* for(int i = 0; i < spectrumLength i++){
			System.out.println(testPower.getSpectrum(i));
		}  // bara kollar om jag faktiskt fått ett spektrum
		*/
		
		
		int spectrumLength = testPower.getSpectrum().length;
	
			
		//Skapar nätverk utan några dolda lager, linjär Act. func.
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, false, spectrumLength));
		network.addLayer(new BasicLayer(100));
		network.addLayer(new BasicLayer(spectrumLength));
		network.getStructure().finalizeStructure();
		network.reset();
		
		
		// Skapar datan
		double[][] inData = new double[1][spectrumLength];
		double[][] utData = new double[1][spectrumLength];
		
		for(int i = 0; i < spectrumLength; i++){
			inData[0][i] = 100*Math.random();
			utData[0][i] = testPower.getSpectrum(i);
		} 
		
		System.out.println("Indata length: " + spectrumLength);
		NeuralDataSet trainingSet = new BasicNeuralDataSet(inData, utData);
		
		
		// skapar binfilen från MLData
		File outFile = new File("Data/Network/test.bin");
		NeuralNet.createBin(trainingSet, outFile);
	
		// skapar en "BufferedReader" från .bin-filen
		BufferedMLDataSet buffSet = new BufferedMLDataSet(outFile);
		
		Iterator itr = buffSet.iterator();
		MLDataPair pair; 
		pair = (MLDataPair) itr.next();// itr.next() hämtar nästa MLDataPair (en inputarray och en outputarray fast i MLData-format)
		System.out.println(""+ pair.getIdeal().getData(1));
		
				
		/*
		
		
		// train the neural network
		final Train train = new ResilientPropagation(network, trainingSet);
		train.addStrategy(new RequiredImprovementStrategy(5)); // reset if improve is less than 1% over 5 cycles
		int epoch = 1;
		do {
			train.iteration();
			System.out.println(
					"Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.01); 
		
		
		// test the neural network
		System.out.println("Neural Network Results:");
		for(MLDataPair pair: trainingSet ) {
			final MLData output = network.compute(pair.getInput()); // network.compute(MlData) använder nätverket
			double[] inpArray = pair.getInputArray();	// Skriver ut hela arrayen
			for(int i = 0;i<spectrumLength;i++){
				System.out.print( " " + inpArray[i]); 
			}
			System.out.println();
			System.out.println("ideal=" + pair.getIdeal().getData(0) + ", actual=" + output.getData(0)); // instanceOfMldata.getData(index i) ger utdata nbr i.
		}	
		*/
				
	}
}