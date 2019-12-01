package com.datastructures.coursework.hashFunction;

import com.datastructures.coursework.api.HashFunction;
import com.datastructures.coursework.api.HashFunctionGenerator;
import com.datastructures.coursework.utils.HashingUtils;
import com.datastructures.coursework.utils.RandomGenerator;

public class UniversalHashFunctionGenerator implements HashFunctionGenerator {

    private RandomGenerator randomGenerator;
    private HashingUtils hashingUtils;

    public UniversalHashFunctionGenerator(RandomGenerator randomGenerator, HashingUtils hashingUtils) {
        this.randomGenerator = randomGenerator;
        this.hashingUtils = hashingUtils;
    }

    @Override
    public HashFunction generateNew(Integer maximumSize, Integer universeSize) {
        if (hashingUtils.getMaxN()<maximumSize){
            throw new RuntimeException("Primes not available after maxN: "+ hashingUtils.getMaxN());
        }
        Integer a = randomGenerator.uniformDistribution(universeSize);
        Integer prime = hashingUtils.getNextPrime(maximumSize);
        return new DotUniversalHashFunction(prime,a);
    }
}
