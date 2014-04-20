package kandidathuvudprogram;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utskrift {

	public static void write (String filename, double[] x) throws IOException{
		BufferedWriter outputWriter = null;

		final File file = new File(filename);
		final File parent_directory = file.getParentFile();

		if (null != parent_directory)
		{
			parent_directory.mkdirs();
		}

		outputWriter = new BufferedWriter(new FileWriter(file));

		for (int i = 0; i < x.length; i++) {
			outputWriter.write(Double.toString(x[i]));
			outputWriter.newLine();
		}
		outputWriter.flush();  
		outputWriter.close();  
	}


	public static void write2Matrix (String filename, double[][] x, String delimiter) throws IOException{
		BufferedWriter outputWriter = null;

		final File file = new File(filename);
		final File parent_directory = file.getParentFile();

		if (null != parent_directory)
		{
			parent_directory.mkdirs();
		}

		outputWriter = new BufferedWriter(new FileWriter(file));
		for (int a = 0; a<x.length; a++){
			for (int i = 0; i < x[a].length; i++) {
				// Maybe:
					// outputWriter.write(x[i]+"");
			// Or:
				outputWriter.write(Double.toString(x[a][i]));
				outputWriter.write(delimiter);
			}
			outputWriter.newLine();
		}
		outputWriter.flush();  
		outputWriter.close();  
	}

	// Skriver ut varje värde i en array
	public static void printArray(double array[]){
		for (int i=0; i<array.length; i++){
			System.out.print("["+i+"]: " + array[i]);
		}
		System.out.println("");
	}

	public static void printArray(double array[], int l){
		for (int i=0; i<l; i++){
			System.out.print("["+i+"]: " + array[i]);
		}
		System.out.println("");
	}

	public static void printArray(int array[]){
		for (int i=0; i<array.length; i++){
			System.out.print("["+i+"]: " + array[i]);
		}
		System.out.println("");
	}

}
