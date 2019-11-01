package com.anantsimran.coursework.model;

public class Average {
    private int total;
    private long sum;

    public Average(int total, long sum) {
        this.total = total;
        this.sum = sum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }
    public double getAverage(){
        return (double)sum/total;
    }
}
