package com.datastructures.coursework.plotter;

import com.datastructures.coursework.model.Coordinate;
import org.jfree.ui.RefineryUtilities;

import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

public class HashTablePlotter {

    public void plot(Hashtable<Double, Result> results){
        List<Coordinate> inserts = results.entrySet().stream()
                .map(r-> new Coordinate( Math.log((double)r.getValue().getTimeTaken()/r.getValue().getTotal()), Math.log(r.getKey()))).collect(Collectors.toList());
        XYPlotter xyPlotter= new XYPlotter("Inserts",inserts);
        xyPlotter.pack( );
        RefineryUtilities.centerFrameOnScreen( xyPlotter );
        xyPlotter.setVisible( true );
    }

}
