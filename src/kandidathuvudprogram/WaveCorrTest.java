package kandidathuvudprogram;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

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
import org.encog.ml.data.buffer.BufferedMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.RequiredImprovementStrategy;
public class WaveCorrTest {

	public static double maxValue(double[] matrix) {
		double max = 0;
		for(int i = 0;i<matrix.length;i++){
			for(int j = 0; j<matrix.length; j++){
				if (matrix[i] > max)
					max = matrix[i];
			}
		}
			
		
		return max;
	}
	
	
	public static double maxValue(double[][] matrix) {
		double max = 0;
		for(int i = 0;i<matrix.length;i++){
			for(int j = 0; j<matrix[1].length; j++){
				if (matrix[i][j] > max)
					max = matrix[i][j];
			}
		}
			
		
		return max;
	}
	
	public static void main(final String args[]) {
		
		double alpha = 0.99;
		String window = "rectangular";
				
		String[] dates = {"2014-01-06"};
		Filemanager.createBin(dates, alpha, window,0,15);
		
		
		// skapar en "BufferedReader" från .bin-filen
		BufferedMLDataSet buffSet = new BufferedMLDataSet(new File("Data/Network/trainingData.bin"));
		
		Iterator<MLDataPair> itr = buffSet.iterator(); 
		Iterator<MLDataPair> itr2 = buffSet.iterator();
		double[][] tmpIdeal = new double[(int) buffSet.getRecordCount()][buffSet.getIdealSize()];  //skapar en double[][] att ha för test från buffSet.
		
		for(int i = 0; i < buffSet.getRecordCount(); i++){
			tmpIdeal[i] = itr.next().getIdealArray();
		}
				
		Chart.useRelevantChart(tmpIdeal[3], "SpectrumPlot", alpha, window, 16384);
		System.out.println(" IdealMax: " + maxValue(tmpIdeal));	
		
		
		
		MLDataPair tmppair = itr2.next();
		// Skapar nätverket	
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, false, tmppair.getInput().size()));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 150));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, tmppair.getIdeal().size()));
		network.getStructure().finalizeStructure();
		network.reset();
		
		
		// train the neural network
		final MLTrain train = new ResilientPropagation(network, buffSet);
		// train.addStrategy(new RequiredImprovementStrategy(1000)); // reset if improve is less than 1% over 5 cycles
		int epoch = 1;
		double[] error = new double[5000];
		train.iteration();
		do {
			train.iteration();
			System.out.println(
					"Epoch #" + epoch + " Error:" + train.getError());
			error[epoch - 1] = train.getError();
			epoch++;
		} while(train.getError() > 0.0005); 
		
		double[] testdata = new double[buffSet.getIdealSize()]; 
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
		try {
			Utskrift.write("Data/Matlabfiler/error.txt", error);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
