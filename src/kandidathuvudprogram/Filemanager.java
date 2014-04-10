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
	 * @param divider Delar alla värden med en faktor eqto divider. Standard är divider = 0 vilket delar med 45000.
	 * @param datum
	 * @return
	 */

	public static double[][] readGravFileInParts(String datum, double divider) {
		
		if (divider == 0)
			divider = 1;
		
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

		double data[][] = new double[splitDataTime.length][splitDataTime[0].length];

		for (int i = 0; i < 4; i++) {
			for (int k = 0; k < data[i].length; k++) {
				data[i][k] = Double.parseDouble(splitDataTime[i][k])/divider;
			}
			System.out.println("GraviData " + i  + " är: " + data[i].length + " punkter lång"); // längdtest 1
		}
		Chart.useChart(data[0], "GravTest", 0.99, "win");
		return data;
	}

	/**
	 * 
	 * @param datum t.ex "2014-01-05"
	 * @param startTid t.ex "06"
	 * @param divider Delar alla värden med en faktor eqto divider. Standard är divider = 0 vilket delar med 20.2.
	 * @return
	 */
	public static double[][] readWaveFile(String datum, double divider){

		if (divider == 0)
			divider = 20.2;
		double data[][]=null; 

		for (int i = 0; i<4; i++){
			String timestr = "" + (100+6*i) ; // fulhaxxar fram 00, 06, 12 ,18 som strängar
			String fil = "wavedata/" + datum.replaceAll("-", "") + "_" + timestr.substring(1) + ".tsv";
			System.out.println("wavedata/" + datum.replaceAll("-", "") + "_" + timestr.substring(1) + ".tsv");
			Import imp = new Import();
			String dataTime[], dataValue[];  

			dataTime = imp.importWhole(fil);
			dataValue = new String[dataTime.length];
			String[] temp;
			
			if(i == 0){
				data = new double[4][dataTime.length-1];
			}
				
			for (int j=0; j<dataTime.length-1; j++){ // varför -1? 
				temp = dataTime[j].split("	");
				dataValue[j] = temp[temp.length-1];
				dataTime[j] = temp[0];
			}
			
			for (int k=0; k<data[i].length; k++){
				data[i][k] = Double.parseDouble(dataValue[k]);
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
		double[][] wavedata; 
		
		
		//Test
		
		double [] testdata =  new double [16384];
		for(int i = 0; i < testdata.length/2; i++){
			testdata [i] = Math.cos(0.3*i*2*Math.PI/testdata.length);	
		}
 		
		//
		
		for (int i = 0; i < datum.length; i++) {
			wavedata = readWaveFile(datum[i],0);
			gravdata = readGravFileInParts(datum[i],0);
			for (int j = 0; j < 4; j++) {
				PowerSpectrum spectrum = new PowerSpectrum(testdata, alpha, win, 4);
				set.add(new BasicMLData(wavedata[j]), new BasicMLData(spectrum.getRelevantSpectrum()));
				System.out.println("GraviData " + j  + " är: " + gravdata[j].length + " punkter lång"); // längdtest 2
				System.out.println("SpectrumData " + j  + " är: " + spectrum.getSpectrum().length + " punkter lång"); // längdtest 1
			}
		}
		
		
		
		System.out.println("Inputsize:" + set.getInputSize());
		System.out.println("Idealsize:" + set.getIdealSize());

		NeuralDataSetCODEC codec = new NeuralDataSetCODEC(set);

		BinaryDataLoader loader = new BinaryDataLoader(codec);

		loader.external2Binary(binFile);
	}

}
