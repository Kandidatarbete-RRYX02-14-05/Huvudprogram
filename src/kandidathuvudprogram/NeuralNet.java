package kandidathuvudprogram;

import java.io.File;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.buffer.BinaryDataLoader;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.ml.data.buffer.codec.*;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.util.csv.CSVFormat;

/**
 * @author hellsten och Emilio 
 *
 */
public class NeuralNet {
	BasicNetwork network;
	int inputSize, outputSize,hiddenNeurons;
	
public NeuralNet(int inputSize, int outputSize, int hiddenNeurons){
	this.hiddenNeurons = hiddenNeurons;
	this.inputSize = inputSize;
	this.outputSize = outputSize;
	
	
	CreateNet();
	Train();
	
	}
	
	/** Method convertToString
	 * @param inFile The path to the in CSV file. American standards (changeable).
	 * @param outFile The path for the binary file created.
	 * @param inputcount the number of input neurons
	 * @param outputcount the number of output neurons
	 * 
	 * Loads a CSV-file and creates a binary file. The first boolean in the codec
	 * constructor is if there is a header in the CSV file. The second is unknown, try both. 
	 *
	 */
	
	/*
	public static void convertToBin(File inFile, File outFile, int inputCount, int outputCount){
		CSVFormat csvFormat = new CSVFormat();
		
		if (csvFormat.getDecimal() == CSVFormat.getDecimalCharacter()){
			System.out.println("ConvertToBin: rätt decimaltecken");
		}
		
		CSVDataCODEC codec = new CSVDataCODEC(inFile, csvFormat, false, inputCount, outputCount, false);
		
		BinaryDataLoader loader = new BinaryDataLoader(codec);
		
		loader.external2Binary(outFile);	
	}
	*/
	
	
	public BasicNetwork getNetwork(){
		return this.network;
	}
	
	public void CreateNet(){
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(inputSize));		//lägger till input lager
		network.addLayer(new BasicLayer(hiddenNeurons));	// lägger till dolda lagret
		network.addLayer(new BasicLayer(outputSize));	// lägger till output lager
		network.getStructure().finalizeStructure();		//säger att inte fler lager kommer
		network.reset();		//slumpar vikterna
	}
	public void Train(){
			//skapar binärfil från 

		/*
		 * BufferedMLDataSet set = new BufferedMLDataSet(file);
		 
		set.beginLoad(2, 1);
		for (int i = 0; i < XOR.XORInput.Length; i++)
		{
		BasicMLData input = new BasicMLData(XOR.XORInput[i]);
		BasicMLData ideal = new BasicMLData(XOR.XORIdeal[i]);
		set.add(input, ideal);
		}
		*/
	
	}
}
