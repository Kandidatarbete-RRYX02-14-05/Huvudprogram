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



/**
 * Used when plotting data from a spectrum, only the first half of the data is plotted. 
 * Frequency on x-axis is adjusted to comply with a sampling distribution of 1Hz and length of data
 * 
 * @param data is the double [] of data to be printed
 * @param dataname is the name of the data and is used for naming the file
 * @param alpha is the alpha used in the filter and is used for naming the file
 * @param window is the window used and is used for naming the file
 * @param maxLength is the FFTlength that is used to correct the frequency of the plot
 */
public class Chart {
    public static void useChart(double [] data, String dataname, double alpha, String window){
        
    	//omega=2PI/L*k
        XYSeries dataChart = new XYSeries("Power Spectrum");
        XYDataset xyDataset = new XYSeriesCollection(dataChart);
        int L=data.length;
        for (int i=0; i<L/2; i++){ // vilka punkter ska plottas?
        	//dataChart.add(i*2*Math.PI/L,Math.log(data[i]));
                dataChart.add(1000*i/L,Math.log(data[i]));
        }
        JFreeChart chart = ChartFactory.createXYLineChart
                     ("Power Spectrum",  // Title
                      "Frequency in mHz",           // X-Axis label
                      "Amplitude",           // Y-Axis label
                      
                      xyDataset             // Show legend
                     );
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.white);
        try {
            ChartUtilities.saveChartAsJPEG(new File(dataname + "_" + alpha + "_" + window + ".jpg"), chart, 1100, 800);
        } catch (IOException ex) {
            Logger.getLogger(TestChart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    /**
     * Used when plotting data that goes between 0.03hz and 0.3hz that comes from getRelevantSpectrum
     * Frequency on x-axis is adjusted to comply with a sampling distribution of 1Hz and length of maxLength double

     * 
     * @param data is the double [] of data to be printed
     * @param dataname is the name of the data and is used for naming the file
     * @param alpha is the alpha used in the filter and is used for naming the file
     * @param window is the window used and is used for naming the file
     * @param maxLength is the FFTlength that is used to correct the frequency of the plot
     */
public static void useRelevantChart(double [] data, String dataname, double alpha, String window, double maxLength){
        
    	//omega=2PI/L*k
        XYSeries dataChart = new XYSeries("Power Spectrum");
        XYDataset xyDataset = new XYSeriesCollection(dataChart);
        int L=data.length;
        for (int i=0; i<L; i++){ // vilka punkter ska plottas?
        	//dataChart.add(i*2*Math.PI/L,Math.log(data[i]));
                dataChart.add((0.03+i)*1000/maxLength,Math.log(data[i]));
        }
        JFreeChart chart = ChartFactory.createXYLineChart
                     ("Power Spectrum",  // Title
                      "Frequency in mHz",           // X-Axis label
                      "Amplitude",           // Y-Axis label
                      
                      xyDataset             // Show legend
                     );
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.white);
        try {
            ChartUtilities.saveChartAsJPEG(new File(dataname + "_" + alpha + "_" + window + ".jpg"), chart, 1100, 800);
        } catch (IOException ex) {
            Logger.getLogger(TestChart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

