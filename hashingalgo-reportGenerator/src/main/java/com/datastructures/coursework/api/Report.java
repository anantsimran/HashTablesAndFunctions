package com.datastructures.coursework.api;

import com.datastructures.coursework.model.Coordinate;

import java.util.Iterator;
import java.util.List;

public interface Report {
    void generateReport(Iterator<Coordinate> coordinates, String xAxisTitle, String yAxisTitle, Transformer xTransformer, Transformer yTransformer);

}
