package com.datastructures.coursework.model;

import java.util.ArrayList;
import java.util.List;

public class TimeCount {

    private Long timeTaken;
    private Integer operationsPerformed;
    private ActivityType activityType;

    private List<TimeCount> subTimes;

    public TimeCount(Long timeTaken, Integer operationsPerformed, ActivityType activityType) {
        this.timeTaken = timeTaken;
        this.operationsPerformed = operationsPerformed;
        this.activityType = activityType;
    }

    public Long getTimeTaken() {
        return timeTaken;
    }

    public Integer getOperationsPerformed() {
        return operationsPerformed;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public List<TimeCount> getSubTimes() {
        return subTimes;
    }

    public void setSubTimes(List<TimeCount> subTimes) {
        this.subTimes = subTimes;
    }

    public void addSubTime(TimeCount timeCount){
        if(this.subTimes==null){
            subTimes= new ArrayList<>();
        }
        subTimes.add(timeCount);
    }


}
