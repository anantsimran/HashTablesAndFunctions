package com.anantsimran.coursework.plotter;

import com.anantsimran.coursework.model.Average;
import com.anantsimran.coursework.model.Coordinate;
import com.anantsimran.coursework.model.OperationType;
import org.jfree.ui.RefineryUtilities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class HashTablePlotter {

    public <R> void plot1(Hashtable<Integer, Average> results, String title){
        Iterator it = results.entrySet().iterator();
        List<Coordinate> inserts = results.entrySet().stream()
                .map(r-> new Coordinate( r.getValue().getAverage(), r.getKey().doubleValue())).collect(Collectors.toList());

        XYPlotter xyPlotter= new XYPlotter("Inserts",inserts);
        xyPlotter.pack( );
        RefineryUtilities.centerFrameOnScreen( xyPlotter );
        xyPlotter.setVisible( true );
    }

    public <R> void plot2(Hashtable<Double, Average> results){
        Iterator it = results.entrySet().iterator();
        List<Coordinate> inserts = results.entrySet().stream()
                .map(r-> new Coordinate( r.getValue().getAverage(), r.getKey())).collect(Collectors.toList());

        XYPlotter xyPlotter= new XYPlotter("Inserts",inserts);
        xyPlotter.pack( );
        RefineryUtilities.centerFrameOnScreen( xyPlotter );
        xyPlotter.setVisible( true );
    }

}
