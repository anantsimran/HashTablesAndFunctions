package com.datastructures.coursework.model;

import java.util.ArrayList;
import java.util.List;

public class TimedValue<R> {
    private R value;
    private Long timeTaken;
    private Long operationsPerformed;
    private ActivityType activityType;

    private List<TimedValue> subTimes;


    public TimedValue(R value, Long timeTaken, Long operationsPerformed, ActivityType activityType) {
        this.value = value;
        this.timeTaken = timeTaken;
        this.operationsPerformed = operationsPerformed;
        this.activityType = activityType;
    }

    public R getValue() {
        return value;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public Long getOperationsPerformed() {
        return operationsPerformed;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public List<TimedValue> getSubTimes() {
        return subTimes;
    }

    public void setSubTimes(List<TimedValue> subTimes) {
        this.subTimes = subTimes;
    }

    public void addSubTime(TimedValue timedValue){
        if(this.subTimes==null){
            subTimes= new ArrayList<>();
        }
        subTimes.add(timedValue);
    }

}
