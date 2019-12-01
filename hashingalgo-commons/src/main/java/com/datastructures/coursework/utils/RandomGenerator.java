package com.datastructures.coursework.utils;

import java.util.Random;

public class RandomGenerator {
    Random random;

    public RandomGenerator() {
        random = new Random(System.currentTimeMillis());
    }

    public int uniformDistribution(int n){
        return random.nextInt(n);
    }
}
