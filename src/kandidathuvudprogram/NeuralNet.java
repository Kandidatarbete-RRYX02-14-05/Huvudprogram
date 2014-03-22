package kandidathuvudprogram;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class NeuralNet {
	BasicNetwork network;
	double[] input,output;
	int hiddenNeurons;
	public NeuralNet(double[] input, double[] output, int hiddenNeurons){
	this.hiddenNeurons=hiddenNeurons;
	this.input=input;
	this.output=output;
	
	
	CreateNet();
	Train();
	
	}
	
	
	public void CreateNet(){
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(input.length));		//lägger till input lager
		network.addLayer(new BasicLayer(hiddenNeurons));	// lägger till dolda lagret
		network.addLayer(new BasicLayer(output.length));	// lägger till output lager
		network.getStructure().finalizeStructure();		//säger att inte fler lager kommer
		network.reset();
	}
	public void Train(){
	
	}
}
