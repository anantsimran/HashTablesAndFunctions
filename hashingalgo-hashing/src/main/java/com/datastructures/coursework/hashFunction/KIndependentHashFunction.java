package com.datastructures.coursework.hashFunction;

import com.datastructures.coursework.api.HashFunction;
import com.datastructures.coursework.model.TimedValue;

import java.util.List;

public class KIndependentHashFunction implements HashFunction {

    private int k;
    private int m;
    private int p;
    private List<Integer> a;

    public KIndependentHashFunction(int k, int m, int p, List<Integer> a) {
        this.k = k;
        this.m = m;
        this.p = p;
        this.a = a;
    }

    Integer getMultiplyModulus(int a, int b, int p){
        Long mul= (long)a*b;
        Long remainder= (mul%p);
        return remainder.intValue();
    }

    Integer getPowerModulus(int a, int i, int p){
        if(i==0){
            return 1;
        }
        Integer modulus = 1;
        while(i>0){
            modulus = getMultiplyModulus(modulus,a,p);
            i--;
        }
        return modulus;
    }

    @Override
    public TimedValue<Integer> getHash(int input) {
        return null;
    }
}
