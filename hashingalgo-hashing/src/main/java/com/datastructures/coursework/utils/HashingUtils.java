package com.datastructures.coursework.utils;

import java.util.Collections;
import java.util.List;

public class HashingUtils {
    private List<Integer> primes;
    private int maxN;

    public List<Integer> getPrimes() {
        return primes;
    }

    public int getMaxN() {
        return maxN;
    }

    public HashingUtils(List<Integer> primes, int maxN) {
        this.primes = primes;
        this.maxN = maxN;
    }

    public int getNextPrime(int n){
        if (n> maxN){
            throw new IllegalArgumentException("n cannot be bigger than maxN: "+maxN);
        }
        int index = Collections.binarySearch(primes, n);
        if (index<0){
            return primes.get(-index +1);
        }
        return primes.get(index);
    }
}
