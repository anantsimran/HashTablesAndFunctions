package com.datastructures.coursework.utils;

import com.datastructures.coursework.model.ActivityType;
import com.datastructures.coursework.model.TimeCount;

import java.util.Optional;

public class TimeCountUtils {

    public Optional<TimeCount> getActivityTimeCount(TimeCount timeCount, ActivityType activityType){
        if (timeCount.getActivityType().equals(activityType)){
            return Optional.of(timeCount);
        }
        for(TimeCount subTimeCount: timeCount.getSubTimes()){
            if (subTimeCount.getActivityType().equals(activityType)){
                return Optional.of(subTimeCount);
            }
        }
        return Optional.empty();
    }


}
