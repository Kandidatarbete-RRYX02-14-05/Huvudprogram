package kandidathuvudprogram;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Utskrift {
	
	public static void write (String filename, double[] x) throws IOException{
		  BufferedWriter outputWriter = null;
		  outputWriter = new BufferedWriter(new FileWriter(filename));
		  for (int i = 0; i < x.length; i++) {
		    // Maybe:
		   // outputWriter.write(x[i]+"");
		    // Or:
		    outputWriter.write(Double.toString(x[i]));
		    outputWriter.newLine();
		  }
		  outputWriter.flush();  
		  outputWriter.close();  
		}

}
