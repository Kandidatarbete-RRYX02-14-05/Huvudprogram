package kandidathuvudprogram;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

public class ColorMap {

	public static void main(String[] args) {

		WaveCorrTest WTC = new WaveCorrTest(new File("Data/Network/Network-NbrNeurons-50-NbrIterations-50"));
		double[][] SAVE = new double[85][35]; 
		for (int i = -70; i<15; i++){
			for (int j = 35; j< 70; j++){
				SAVE[i+70][j-35] = WTC.pyramidTest(new Point(i,j));
				}
			System.out.println(i);
		}
		try {
			Utskrift.write2Matrix("MEGASAVEFILE5.txt", SAVE, "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("klar");
	}


}