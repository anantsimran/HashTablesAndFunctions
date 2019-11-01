package com.anantsimran.coursework.model;

public class TimedValue<R> {
    private R value;
    private long timeTaken;

    public TimedValue(R value, long timeTaken) {
        this.value = value;
        this.timeTaken = timeTaken;
    }

    public R getValue() {
        return value;
    }

    public long getTimeTaken() {
        return timeTaken;
    }
}
