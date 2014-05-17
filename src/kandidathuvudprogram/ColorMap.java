package kandidathuvudprogram;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

public class ColorMap {

	public static void main(String[] args) {

		WaveCorrTest WTC = new WaveCorrTest(new File("Data/Network/Network-NbrNeurons-50-NbrIterations-50"));
		try {
			Utskrift.write("Wavish.txt", WTC.buffSet.get(0).getInputArray());
		} catch (IOException e1) {
			System.out.println("JF");// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		double[][] SAVE1 = new double[85][35]; 
		double[][] SAVE2 = new double[85][35];
		double[] tmpsave;
		for (int i = -70; i<15; i++){
			for (int j = 35; j< 70; j++){
			tmpsave = WTC.pyramidTest(new Point(i,j));
				SAVE1[i+70][j-35] = tmpsave[0];
				SAVE2[i+70][j-35] = tmpsave[1];
				}
			System.out.println(i);
		}
		try {
			Utskrift.write2Matrix("MEGASAVEFILE30-70.txt", SAVE1, "\t");
			Utskrift.write2Matrix("MEGASAVEFILE100-200.txt", SAVE2, "\t");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("klar");
	}


}