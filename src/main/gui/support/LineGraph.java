package main.gui.support;

import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


@SuppressWarnings("serial")
public class LineGraph extends JPanel {

	public LineGraph(int [] data, String subject){
		DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
		
		for (int i=0; i<20; i++){
			line_chart_dataset.addValue( data[i] , "" , i*5 + "-" + (i+1)*5 );
		}

		JFreeChart lineChartObject = ChartFactory.createLineChart("Distribution of marks of subject "+subject, "Marks ranges", "Frequency", line_chart_dataset, PlotOrientation.VERTICAL, false, false, false);
		JFreeChart lineChartObject2 = ChartFactory.createLineChart("", "", "Frequency", line_chart_dataset, PlotOrientation.HORIZONTAL, false, false,false);
		
		
		int width = 1200;    /* Width of the image */
		int height = 480;   /* Height of the image */ 
		File lineChart = new File(System.getProperty("java.io.tmpdir")+subject+".jpg"); 
		
		int smallWidth = 450;
		int smallHeight = 500;
		File lineChartSmall = new File(System.getProperty("java.io.tmpdir")+subject+"s.jpg"); 
		
		
		try {
			ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, width, height);
			ChartUtilities.saveChartAsJPEG(lineChartSmall, lineChartObject2, smallWidth, smallHeight);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
