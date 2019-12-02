package com.datastructures.coursework.hashFunction;

import com.datastructures.coursework.api.HashFunction;
import com.datastructures.coursework.model.ActivityType;
import com.datastructures.coursework.model.TimeCount;
import com.datastructures.coursework.model.TimedValue;

public class ModulusHashFunction implements HashFunction {
    private Integer bound;


    public ModulusHashFunction(Integer bound) {
        this.bound = bound;

    }


    @Override
    public TimedValue<Integer> getHash(int input) {
        long startTime = System.nanoTime();

        if (bound==0){
            throw new RuntimeException("Maximum bound is zero. Cannot proceed");
        }
        return new TimedValue<>(input%bound,
                new TimeCount(System.nanoTime()- startTime,null, ActivityType.HASH_FUNCTION)
        );
    }




}
