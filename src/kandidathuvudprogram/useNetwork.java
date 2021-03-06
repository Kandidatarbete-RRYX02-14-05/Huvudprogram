package kandidathuvudprogram;

import java.io.IOException;

import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.buffer.BufferedMLDataSet;

public class useNetwork {

	public static void createFilesForAllDays(WaveCorrTest WTC){ //nyttnamn
		/*	int IdealDataSize = (WTC.buffSet.get(0).getIdeal()).getData().length;
		double[] mse = new double[(int) WTC.buffSet.getRecordCount()];
		for (int i = 0; i<(int) WTC.buffSet.getRecordCount(); i++){

			for (int j = 0; j < IdealDataSize; j++){
				mse[i] += Math.pow(((WTC.buffSet.get(i).getIdeal()).getData()[j]-WTC.network.compute(WTC.buffSet.get(i).getInput()).getData()[j]),2);
			}

			mse[i] = mse[i]/IdealDataSize;
		}
		 */

		for (int i = 0; i < WTC.buffSet.getRecordCount(); i++){
			try {
				Utskrift.write("Data/Matlabfiler/TrainTest" + i + "fromGravimeter", (WTC.buffSet.get(i).getIdeal()).getData());
				Utskrift.write("Data/Matlabfiler/TrainTest" + i + "fromNet", WTC.network.compute(WTC.buffSet.get(i).getInput()).getData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//Chart.useRelevantChart((WTC.buffSet.get(i).getIdeal()).getData(),"Test" + 2*i, 0.99, "rect");
			//Chart.useRelevantChart(WTC.network.compute(WTC.buffSet.get(i).getInput()).getData(),"Test" + (2*i+1), 0.99, "rect");
		} // 
		/*
		Chart.NormalChart(mse, "mse");

		for(int i = 0; i < mse.length; i++){
			if (mse[i] > 0.001)
				System.out.print( " " + i);
		}
		 */
	}

	public static void createGenErrorFiles(WaveCorrTest WTC){ //nyttnamn
		BufferedMLDataSet set = WTC.networkGenErrorLoad();
		/*
		int IdealDataSize = (set.get(0).getIdeal()).getData().length;
		double[] mse = new double[(int) set.getRecordCount()];
		for (int i = 0; i<(int) set.getRecordCount(); i++){

			for (int j = 0; j < IdealDataSize; j++){
				mse[i] += Math.pow(((set.get(i).getIdeal()).getData()[j]-WTC.network.compute(set.get(i).getInput()).getData()[j]),2);
			}

			mse[i] = mse[i]/IdealDataSize;
		}
*/
		for (int i = 0; i < set.getRecordCount(); i++){
			try {
				Utskrift.write("Data/Matlabfiler/GenTest" + i + "fromGravimeter", (set.get(i).getIdeal()).getData());
				Utskrift.write("Data/Matlabfiler/GenTest" + i + "fromNet", WTC.network.compute(set.get(i).getInput()).getData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//Chart.useRelevantChart((set.get(i).getIdeal()).getData(),"Test" + 2*i, 0.99, "rect");
			//Chart.useRelevantChart(WTC.network.compute(set.get(i).getInput()).getData(),"Test" + (2*i+1), 0.99, "rect");
		} // 
/*
		Chart.NormalChart(mse, "mse");

		for(int i = 0; i < mse.length; i++){
			if (mse[i] > 0.001)
				System.out.print( " " + i);
		}
*/
	}


}
