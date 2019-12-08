package com.datastructures.coursework.model;

public class Coordinate implements Comparable<Coordinate>{
    private Double X, Y;

    public Coordinate(Double x, Double y) {
        X = x;
        Y = y;
    }

    public Double getX() {
        return X;
    }

    public void setX(Double x) {
        X = x;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double y) {
        Y = y;
    }

    @Override
    public int compareTo(Coordinate o) {
        if(this.getX().compareTo(o.getX())==0){
            return this.getY().compareTo(o.getY());
        }
        return this.getX().compareTo(o.getX());
    }

}
