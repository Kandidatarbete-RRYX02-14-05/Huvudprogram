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
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
public class WaveCorrTest {

	
	
	
	public static void main(final String args[]) {
		
		
		
	
		Filemanager.createBin("2010-05-10", 0.99, "Hanning");
		
		
	
		// skapar en "BufferedReader" från .bin-filen
		BufferedMLDataSet buffSet = new BufferedMLDataSet(new File("Data/Network/100510.bin"));
		
		Iterator<MLDataPair> itr = buffSet.iterator();
		MLDataPair tmppair =  (MLDataPair) itr.next();// itr.next() hämtar nästa MLDataPair (en inputarray och en outputarray fast i MLData-format)
		
		
				
		
		
		// Skapar nätverket	
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, false, tmppair.getInput().size()));
		//network.addLayer(new BasicLayer(100));
		network.addLayer(new BasicLayer( new ActivationLinear(), true, tmppair.getIdeal().size()));
		network.getStructure().finalizeStructure();
		network.reset();
		
		
		// train the neural network
		final MLTrain train = new ResilientPropagation(network, buffSet);
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
		for(MLDataPair pair: buffSet ) {
			final MLData output = network.compute(pair.getInput()); // network.compute(MlData) använder nätverket
			double[] inpArray = pair.getInputArray();	// Skriver ut hela arrayen
			for(int i = 0;i<2818;i++){
				System.out.print( " " + inpArray[i]); 
			}
			System.out.println();
			System.out.println();
			for (int i = 0;i<5;i++){
				System.out.println("ideal=" + pair.getIdeal().getData(i) + ", actual=" + output.getData(i)); // instanceOfMldata.getData(index i) ger utdata nbr i.
			}
		}	
		
				
	}
}