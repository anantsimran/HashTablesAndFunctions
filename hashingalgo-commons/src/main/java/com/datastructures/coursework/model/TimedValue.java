package com.datastructures.coursework.model;

public class TimedValue<R> {
    private R value;
    private TimeCount timeCount;

    public TimedValue(R value, TimeCount timeCount) {
        this.value = value;
        this.timeCount = timeCount;
    }

    public R getValue() {
        return value;
    }

    public TimeCount getTimeCount() {
        return timeCount;
    }
}
