package com.datastructures.coursework.plotter;

import com.datastructures.coursework.api.Plotter;
import com.datastructures.coursework.api.Transformer;
import com.datastructures.coursework.model.Coordinate;
import org.jfree.ui.RefineryUtilities;

import java.util.*;
import java.util.stream.Collectors;

public class CoordinatePlotter implements Plotter {
    private static final int NUMBER_OF_BINS = 500;

    @Override
    public void plot(Iterator<Coordinate> coordinates, String chartTitle, String xAxisTitle,
                     String yAxisTitle, Transformer xTransformer, Transformer yTransformer) {

        List<Coordinate> points = new ArrayList<>();
        for (Iterator<Coordinate> it = coordinates; it.hasNext(); ) {
            Coordinate coordinate = it.next();
            points.add(new Coordinate(xTransformer.transform(coordinate.getX()), yTransformer.transform(coordinate.getY())));
        }

        TreeMap<Double, Double> transformedCoordinates = transform(points);

        List<Coordinate> transformedPointsList = transformedCoordinates.entrySet().stream()
                .map(v -> new Coordinate(v.getKey(), v.getValue())).collect(Collectors.toList());
        XYPlotter xyPlotter= new XYPlotter(chartTitle, transformedPointsList, xAxisTitle, yAxisTitle);
        xyPlotter.pack( );
        RefineryUtilities.centerFrameOnScreen( xyPlotter );
        xyPlotter.setVisible( true );
    }

    private TreeMap<Double, Double> transform(List<Coordinate> points) {
        Collections.sort( points, (x1, x2) -> {
            int result = Double.compare(x1.getX(), x2.getX());
            return result;
        });

        int partitionSize = points.size()/NUMBER_OF_BINS;
        List<List<Coordinate>> partitions = new LinkedList<>();
        for (int i = 0; i < points.size(); i += partitionSize) {
            partitions.add(points.subList(i,
                    Math.min(i + partitionSize, points.size())));
        }

        TreeMap<Double, Double> mapOfBins = new TreeMap<>();
        for (List<Coordinate> partition: partitions) {
            double averageX = calculateAverage(partition);
            double medianY = calculateMedian(partition);

            mapOfBins.put(averageX, medianY);
        }
        return mapOfBins;
    }

    private double calculateMedian(List<Coordinate> partition) {
        Collections.sort(partition, new Comparator<Coordinate>() {
            public int compare(Coordinate x1, Coordinate x2) {
                int result = Double.compare(x1.getY(), x2.getY());
                return result;
            }
        });

        double median;
//        if (partition.size() % 2 == 0)
//            median = ((double)partition.get(partition.size()/2).getY() + (double)partition.get(partition.size()/2 - 1).getY()/2);
//        else
            median = (double)partition.get(partition.size()/2).getY();
        return median;
    }

    private double calculateAverage(List<Coordinate> partition) {
        double sum = 0;
        if(!partition.isEmpty()) {
            for (Coordinate coordinate : partition) {
                sum += coordinate.getX();
            }
            return sum / partition.size();
        }
        return sum;
    }
}

//    public void plot(Hashtable<Double, Result> results){
//        List<Coordinate> inserts = results.entrySet().stream()
//                .map(r-> new Coordinate( Math.log((double)r.getValue().getTimeTaken()/r.getValue().getTotal()), Math.log(r.getKey()))).collect(Collectors.toList());
//        XYPlotter xyPlotter= new XYPlotter("Inserts",inserts);
//        xyPlotter.pack( );
//        RefineryUtilities.centerFrameOnScreen( xyPlotter );
//        xyPlotter.setVisible( true );
//    }


