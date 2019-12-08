package com.datastructures.coursework.api;

import com.datastructures.coursework.model.Coordinate;

import java.util.Iterator;

public interface Plotter {
    void plot( Iterator<Coordinate> coordinates,
               String chartTitle,
               String xAxisTitle, String yAxisTitle,
               Transformer xTransformer, Transformer yTransformer
               );

}
