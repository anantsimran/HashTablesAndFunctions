package com.datastructures.coursework.hashFunction;

import com.datastructures.coursework.api.HashFunction;

public class ModulusHashFunction implements HashFunction {
    private Integer bound;
    private long totalHashTime;


    public ModulusHashFunction(Integer bound) {
        this.bound = bound;
        totalHashTime =0;

    }


    @Override
    public int getHash(int input) {
        long startTime = System.nanoTime();

        if (bound==0){
            throw new RuntimeException("Maximum bound is zero. Cannot proceed");
        }
        totalHashTime+= System.nanoTime()- startTime;
        return input%bound;
    }

    @Override
    public long getTotalHashTime() {
        return this.totalHashTime;
    }


}
