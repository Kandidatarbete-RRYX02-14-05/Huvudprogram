package kandidathuvudprogram;

import java.io.File;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.buffer.BinaryDataLoader;
import org.encog.ml.data.buffer.codec.NeuralDataSetCODEC;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;

public class Filemanager {

	/**
	 * 
	 * @param datum
	 * @return double[] med gravidata
	 */
	public static double[] readGravFile(String datum) {
		String fil = "gravidata/" + datum.substring(2).replaceAll("-", "")
				+ ".tsf";
		Import imp = new Import();
		String dataTime[], dataValue[];

		dataTime = imp.importWhole(fil);
		dataValue = new String[dataTime.length];
		String[] temp;
		for (int i = 0; i < dataTime.length - 1; i++) {
			temp = dataTime[i].split(" ");
			dataValue[i] = temp[temp.length - 1];
			dataTime[i] = temp[0];
		}

		double data[] = new double[dataTime.length - 1];
		for (int i = 0; i < data.length; i++) {
			data[i] = Double.parseDouble(dataValue[i]);
		}
		;

		return data;
	}

	/**
	 * SKRIV HÄR!
	 * 
	 * @param datum
	 * @return
	 */

	public static double[][] readGravFileInParts(String datum) {
		String fil = "gravidata/" + datum.substring(2).replaceAll("-", "")
				+ ".tsf";
		Import imp = new Import();
		String dataTime[], dataValue[];

		dataTime = imp.importWhole(fil);
		dataValue = new String[dataTime.length];
		String[] temp;
		for (int i = 0; i < dataTime.length - 1; i++) {
			temp = dataTime[i].split(" ");
			dataValue[i] = temp[temp.length - 1];
			dataTime[i] = temp[0];
		}

		String[][] splitDataTime = imp.splitSixHours(dataValue);

		double data[][] = new double[4][dataTime.length - 1];

		for (int i = 0; i < 4; i++) {
			for (int k = 0; k < data.length; k++) {
				data[i][k] = Double.parseDouble(splitDataTime[i][k]);
			}
		}
		return data;
	}

	/**
	 * 
	 * @param set
	 *            MLDataSet som man skapar bin filen ifrån, ex: "2010-05-10"
	 * @param outFile
	 */

	public static void createBin(String[] datum, double alpha, String win) {

		BasicMLDataSet set = new BasicMLDataSet();
		File binFile = new File("Data/Network/trainingData.bin");
		double[][] gravdata;
		for (int i = 0; i < datum.length; i++) {

			gravdata = readGravFileInParts(datum[i]);
			for (int j = 0; j < 4; j++) {
				PowerSpectrum spectrum = new PowerSpectrum(gravdata[j], alpha,
						win, 4);
				set.add(new BasicMLData(spectrum.getRelevantSpectrum()),
						new BasicMLData(spectrum.getRelevantSpectrum()));
			}
		}
		System.out.println("Inputsize:" + set.getInputSize());
		System.out.println("Idealsize:" + set.getIdealSize());

		NeuralDataSetCODEC codec = new NeuralDataSetCODEC(set);

		BinaryDataLoader loader = new BinaryDataLoader(codec);

		loader.external2Binary(binFile);
	}

}
