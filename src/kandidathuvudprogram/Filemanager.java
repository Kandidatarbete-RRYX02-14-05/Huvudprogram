package kandidathuvudprogram;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.buffer.BinaryDataLoader;
import org.encog.ml.data.buffer.codec.NeuralDataSetCODEC;

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
	 * SKRIV HÃ„R!
	 * @param divider Delar alla vÃ¤rden med en faktor eqto divider. Standard Ã¤r divider = 0 vilket delar med 45000.
	 * @param datum
	 * @return
	 */

	public static double[][] readGravFileInParts(String datum) {


		String fil = "gravidata/" + datum.replaceAll("-", "")
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
				data[i][k] = Double.parseDouble(splitDataTime[i][k]);
			}
		}
		Chart.useChart(data[0], "GravTest", 0.99, "win");
		return data;
	}

	/**
	 * 
	 * @param datum t.ex "2014-01-05"
	 * @param startTid t.ex "06"
	 * @param divider Delar alla vÃ¤rden med en faktor eqto divider. Standard Ã¤r divider = 0 vilket delar med 20.2.
	 * @return
	 */
	public static double[][] readWaveFile(String datum, double divider){

		if (divider == 0)
			divider = 20.2;
		double data[][]=null; 

		for (int i = 0; i<4; i++){
			String timestr = "" + (100+6*i) ; // fulhaxxar fram 00, 06, 12 ,18 som strÃ¤ngar
			String fil = "C:\\Users\\Emilio\\git\\Huvudprogram\\wavedata\\removedmissing\\20" + datum + "_" + timestr.substring(1) + ".tsv"; //.replaceAll("-", "")
			Import imp = new Import();
			String dataTime[], dataValue[];  

			dataTime = imp.importWhole(fil);
			dataValue = new String[dataTime.length];
			String[] temp;

			if(i == 0){
				data = new double[4][dataTime.length-1];
			}

			for (int j=0; j<dataTime.length-1; j++){ // varfÃ¶r -1? 
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
	 *            MLDataSet som man skapar bin filen ifrÃ¥n, ex: "2010-05-10"
	 * @param outFile
	 * @throws IOException 
	 */

	public static void createBin(String[] datum, String filnamn, double alpha, String win, double dividerwave, double dividergrav) throws IOException {

		if (dividergrav == 0)
			dividergrav = 13;

		BasicMLDataSet set = new BasicMLDataSet();
		File binFile = new File("Data/Network/" + filnamn + ".bin");
		double[][] gravdata;
		double[][] wavedata; 
		final File file = new File("EarthquakeData_" + filnamn + ".txt");
		final File parent_directory = file.getParentFile();

		if (null != parent_directory)
		{
			parent_directory.mkdirs();
		}
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(file));

		//GetDataHgsChalmers.downloadGraviData(datum);
		for (int i = 0; i < datum.length-1; i++) {
			wavedata = readWaveFile(datum[i],0);
			gravdata = readGravFileInParts(datum[i]);
			for (int j = 0; j < 4; j++) {
				if(!isEarthquake(gravdata[j])){
					PowerSpectrum spectrum = new PowerSpectrum(gravdata[j], alpha, win, 80);
					set.add(new BasicMLData(wavedata[j]), new BasicMLData(spectrum.getRelevantSpectrum(dividergrav)));
				}
				else{
					outputWriter.write(datum[i] +" " + j*6);
					outputWriter.newLine();
					System.out.println(datum[i] +" " + j*6 + " has earthquake");
				}
			}
		}
		outputWriter.close();  

		NeuralDataSetCODEC codec = new NeuralDataSetCODEC(set);

		BinaryDataLoader loader = new BinaryDataLoader(codec);

		loader.external2Binary(binFile);
	}


	public static Point[] createPointRepresentation(String mapName){
		Import imp = new Import();
		String[] tmpString = imp.importWhole(mapName);
		Point[] point = new Point[tmpString.length];
		for (int i = 0 ; i < tmpString.length - 1; i++ ){
			point[i] = new Point((int)Double.parseDouble(tmpString[i].split("	")[0]),(int)Double.parseDouble(tmpString[i].split("	")[1]));
		}
		return point;
	}

	public static int[] choosePoints( Point[] targetPoints ){

		Point[] data = createPointRepresentation("wavedata/removedmissing/map.tsv");

		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < data.length-1; i++){
			for(int j = 0; j < targetPoints.length; j++){
				if(data[i].equals(targetPoints[j]))
					list.add(i);
			}
		}
		int[] output = new int[list.size()];
		for(int i = 0; i < list.size(); i++){
			output[i] = list.get(i).intValue(); // Vet inte exakt varfÃ¶r men blir rÃ¤tt
		}
		return output;


	}
	/**
	 * Checks if data has earthquakes by comparing by checking two consecutive 
	 * 10 minute RMS that overlap with half their data
	 * and sees if RMS changes drastically.
	 * @param data Data to be checked for earthquakes.
	 * @return isEarthquake
	 */
	public static boolean isEarthquake(double[] data){
		if (data.length<=300)
			throw new IllegalArgumentException("Empty/Too small RMS data");
		double temp1=rms(data,0,600-1);
		double temp2;
		if(max(data)>700){		//stora värden innebär j-bavning
			return true;}
		for (int i = 20; i<data.length-600; i += 600){
			temp2=rms(data,i,i+599);
			if(temp2>temp1*3||temp1>temp2*3||temp2>175){	//ser till att kvoten inte är för stor eller att rms är för stor
				return true;
			}
			temp1=temp2;
		}
		return false;
	}
	public static double max(double[] data){
		double maximum=0;
		for(double val:data){
			if (Math.abs(val)>maximum){
				maximum=Math.abs(val);
			}	
		}
		return maximum;
	}
	public static double rms(double[] data, int start, int end){
		double sum=0;
		for(int i=start; i<=end; i++){
			sum+=Math.pow(data[i], 2);
		}
		return Math.sqrt(sum/data.length);
	}
}
