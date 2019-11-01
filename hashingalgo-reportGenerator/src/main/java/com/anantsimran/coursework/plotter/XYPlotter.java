package com.anantsimran.coursework.plotter;

import com.anantsimran.coursework.model.Coordinate;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import java.util.List;



public class XYPlotter extends ApplicationFrame {

    private XYSeriesCollection getDataset(String title, List<Coordinate> coordinates){
        XYSeries xySeries = new XYSeries(title);
        coordinates.forEach(
                c -> xySeries.add(new XYDataItem(c.getX(), c.getY()))
        );
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(xySeries);
        return dataset;
    }
     private DefaultCategoryDataset createDataset(String title, List<Coordinate> coordinates ) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        coordinates.forEach(
                 c -> dataset.addValue(c.getX(),title, c.getY())
         );
        return dataset;
    }

    public XYPlotter(String title, List<Coordinate> coordinates) {
        super(title);
        JFreeChart lineChart = ChartFactory.createLineChart(
                title,
                "n","time",
                createDataset( title,  coordinates),
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
    }



}
