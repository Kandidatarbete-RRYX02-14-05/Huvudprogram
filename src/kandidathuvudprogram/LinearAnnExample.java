package kandidathuvudprogram;
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
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
public class LinearAnnExample {

	// Semirandom data, tre tal mellan 1 & 2 
	static double[][] data = {{0.0,0.0,0.0}, {0.0,0.0,1.0}, {0.0,1.0,0.0}, {1.0,0.0,0.0}, {1.0,1.0,0.0}, {1.0,0.0,1.0},	{0.0,1.0,1.0},	{1.0,1.0,1.0},
		{0.4,0.2,0.3}, {0.5,0.5,1.2}, {0.7,1.2,0.4}, {1.2,0.4,0.6}, {1.2,1.4,0.8}, {1.1,0.0,1.5},	{0.4,1.2,1.6},	{1.4,1.3,1.5}};
	
	// Summan av respektive datamängd
	static double[][] ideal = {{0.0},{1.0},{1.0},{1.0},{2.0},{2.0},{2.0},{3.0},
		{0.9},{2.2},{2.3},{2.3},{3.4},{2.6},{3.2},{4.2}};
	
	// Testdata som används i "Eriks test" 
	static double[] testIn = {0.4,0.2,0.6};

	/*
	static double[][] sin;
	static double[][] freq;

	public static void initsin(int nrFunk, int nrVal){
		sin = new double[nrFunk][nrVal];
		freq = new double[nrFunk][1];

		for (int i=0; i<nrFunk; i++){
			freq[i][0] = i+10;
			for (int a=0; a<nrVal; a++){
				sin[i][a] = a*(i+1);
			}
		}
	}
	 */


	public static void main(final String args[]) {
		
		// nbr of in-neurons
		int nbrIn = 3;
		
		//Skapar nätverk utan några dolda lager, linjär Act. func.
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, false, nbrIn));
		//network.addLayer(new BasicLayer(5));
		network.addLayer(new BasicLayer(new ActivationLinear(), true, 1));
		network.getStructure().finalizeStructure();
		network.reset();
		
		
		NeuralDataSet trainingSet = new BasicNeuralDataSet(data, ideal);
		
		
		// train the neural network
		final Train train = new ResilientPropagation(network, trainingSet);
		train.addStrategy(new RequiredImprovementStrategy(8)); // reset if improve is less than 1% over 5 cycles
		int epoch = 1;
		do {
			train.iteration();
			System.out.println(
					"Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.0006); //Bättre än såhär konvergerarden inte. Varför?
		
		
		// test the neural network
		System.out.println("Neural Network Results:");
		for(MLDataPair pair: trainingSet ) {
			final MLData output = network.compute(pair.getInput()); // network.compute(MlData) använder nätverket
			double[] inpArray = pair.getInputArray();	// Skriver ut hela arrayen
			for(int i = 0;i<nbrIn;i++){
				System.out.print( " " + inpArray[i]); 
			}
			System.out.println();
			System.out.println("ideal=" + pair.getIdeal().getData(0) + ", actual=" + output.getData(0)); // instanceOfMldata.getData(index i) ger utdata nbr i.
		}	

		for (int i = 0; i<10; i++){

		}
		BasicNeuralData ndIn = new BasicNeuralData(testIn);  // Gör basicNeuralDasta av double[] testIn för network.compute behöver MLData för att räkna på. 
		MLData ndOut = network.compute(ndIn);
		System.out.println("Eriks test");
		System.out.println("In: " + ndIn.getData(0) + ", " + ndIn.getData(1) + ", " + ndIn.getData(1));
		System.out.println("Ut: " + ndOut.getData(0));
	}
}