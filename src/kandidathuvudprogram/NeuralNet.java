package kandidathuvudprogram;

import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.buffer.BinaryDataLoader;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.ml.data.buffer.codec.ArrayDataCODEC;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

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
	
	
	public void CreateNet(){
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(inputSize));		//lägger till input lager
		network.addLayer(new BasicLayer(hiddenNeurons));	// lägger till dolda lagret
		network.addLayer(new BasicLayer(outputSize));	// lägger till output lager
		network.getStructure().finalizeStructure();		//säger att inte fler lager kommer
		network.reset();		//slumpar vikterna
	}
	public void Train(){
		ArrayDataCODEC codec = new ArrayDataCODEC(input, output);
		BinaryDataLoader loader = new BinaryDataLoader(codec);
		File file
		loader.external2Binary(file);		//skapar binärfil från 

		BufferedMLDataSet set = new BufferedMLDataSet(file);
		set.BeginLoad(2, 1);
		for (int i = 0; i < XOR.XORInput.Length; i++)
		{
		BasicMLData input = new BasicMLData(XOR.XORInput[i]);
		BasicMLData ideal = new BasicMLData(XOR.XORIdeal[i]);
		set.Add(input, ideal);
		}
		
	
	}
}
