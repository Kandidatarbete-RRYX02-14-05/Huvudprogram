package kandidathuvudprogram;


import java.awt.Color;
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
        
        for (int i=0; i<300; i++){ // vilka punkter ska plottas?
        	dataChart.add(i,Math.log(data[i]));
        }
        JFreeChart chart = ChartFactory.createXYAreaChart
                     ("Power Spectrum",  // Title
                      "Frequency",           // X-Axis label
                      "Amplitude",           // Y-Axis label
                      
                      xyDataset             // Show legend
                     );
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.white);
        try {
            ChartUtilities.saveChartAsJPEG(new File("SpectrumChart2.jpg"), chart, 500, 300);
        } catch (IOException ex) {
            Logger.getLogger(TestChart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

