package com.anantsimran.coursework.model;

public class Result {
    private OperationType operationType;
    private int n;
    private long time;

    public Result(OperationType operationType, int n, long time) {
        this.operationType = operationType;
        this.n = n;
        this.time = time;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public int getN() {
        return n;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return operationType + "," + n + "," + time;
    }
}
