package com.datastructures.coursework.hashFunctionGenerator;

import com.datastructures.coursework.api.HashFunction;
import com.datastructures.coursework.api.HashFunctionGenerator;
import com.datastructures.coursework.hashFunction.KIndependentHashFunction;
import com.datastructures.coursework.utils.HashingUtils;
import com.datastructures.coursework.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class KIndependentHashFunctionGenerator implements HashFunctionGenerator {
    private RandomGenerator randomGenerator;
    private HashingUtils hashingUtils;
    private int k;

    public KIndependentHashFunctionGenerator(RandomGenerator randomGenerator, HashingUtils hashingUtils, int k) {
        this.randomGenerator = randomGenerator;
        this.hashingUtils = hashingUtils;
        this.k = k;
    }

    @Override
    public HashFunction generateNew(Integer maximumSize, Integer universeSize) {
        if (hashingUtils.getMaxN()<maximumSize){
            throw new RuntimeException("Primes not available after maxN: "+ hashingUtils.getMaxN());
        }
        Integer prime = hashingUtils.getNextPrime(maximumSize);
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i < this.k; i++) {
            a.add(randomGenerator.uniformDistribution(universeSize));
        }
        while (a.get(0)==0){
            a.set(0, randomGenerator.uniformDistribution(universeSize));
        }
        return new KIndependentHashFunction(this.k,maximumSize,prime,a);

    }
}
