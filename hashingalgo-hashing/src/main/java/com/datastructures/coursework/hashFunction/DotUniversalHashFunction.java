package com.datastructures.coursework.hashFunction;

import com.datastructures.coursework.api.HashFunction;
import com.datastructures.coursework.model.ActivityType;
import com.datastructures.coursework.model.TimeCount;
import com.datastructures.coursework.model.TimedValue;

import java.util.ArrayList;
import java.util.List;

public class DotUniversalHashFunction implements HashFunction {
    private Integer m;
    private Integer a;
    private List<Integer> baseMRepresentation;

    public DotUniversalHashFunction(Integer m, Integer a) {
        this.m = m;
        this.a = a;
        this.baseMRepresentation = getBaseMRepresentation(a,m);
    }

    private List<Integer> getBaseMRepresentation(Integer x, Integer m) {
        List<Integer> representaion = new ArrayList<>();
        int num = x;
        while (num > 0) {
            representaion.add(num % m);
            num /= m;
        }
        return representaion;
    }

    private Integer getSum(List<Integer> representation1, List<Integer> representation2, Integer m){
        long sum=0;
        if(representation1==null || representation2==null){
            throw new RuntimeException();
        }
        int minSize = Math.min(representation1.size(), representation2.size());
        for (int i = 0; i < minSize; i++) {
            sum+= Long.valueOf(representation1.get(i))*representation2.get(i);
        }
        Long mod= sum%m;
        return mod.intValue();
    }

    @Override
    public TimedValue<Integer> getHash(int input) {
        long startTime = System.nanoTime();
        Integer sum = getSum(getBaseMRepresentation(input, m), baseMRepresentation, m);
        return new TimedValue<>(sum,
            new TimeCount(System.nanoTime()- startTime,null, ActivityType.HASH_FUNCTION)
                );
    }

}
