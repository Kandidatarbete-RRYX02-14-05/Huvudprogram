package kandidathuvudprogram;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Emilio (ish) lite Makkan kanske och Erik (nästan)
 */
public class Chart {
    public static void useChart(double [] data){
        
        
        XYSeries dataChart = new XYSeries("Power Spectrum");
        XYDataset xyDataset = new XYSeriesCollection(dataChart);
        
        for (int i=0; i<data.length; i++){
        	dataChart.add(i,data[i]);
        }
        JFreeChart chart = ChartFactory.createXYAreaChart
                     ("Sample XY Chart",  // Title
                      "Height",           // X-Axis label
                      "Weight",           // Y-Axis label
                      xyDataset             // Show legend
                     );
        try {
            ChartUtilities.saveChartAsJPEG(new File("SpectrumChart.jpg"), chart, 500, 300);
        } catch (IOException ex) {
            Logger.getLogger(TestChart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

