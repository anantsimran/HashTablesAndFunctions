package com.anantsimran.coursework;

import com.anantsimran.coursework.algo.ChainHash;
import com.anantsimran.coursework.api.Hash;
import com.anantsimran.coursework.api.HashFunction;
import com.anantsimran.coursework.exception.NotFoundException;
import com.anantsimran.coursework.hashFunction.ModulusHashFunction;
import com.anantsimran.coursework.model.OperationType;
import com.anantsimran.coursework.model.Pair;
import com.anantsimran.coursework.model.Result;
import com.anantsimran.coursework.model.TimedValue;

import java.util.*;

public class Application {

    public static Integer getInsertedRandom(RandomGenerator randomGenerator, List<Integer> inserted){
        Objects.requireNonNull(inserted);
        if (inserted.isEmpty()){
            throw new RuntimeException();
        }
        int number = randomGenerator.uniformDistribution(inserted.size());
        return inserted.get(number);
    }

    public static void main(String[] args){

        RandomGenerator randomGenerator = new RandomGenerator();


        int n = 100000;
        int N = 100000;
        int universeSize = 100000000;
        int searchSpace =100;
        List<Result> results = new ArrayList<>();
        List<Integer> inserted = new ArrayList<>();
        HashFunction hashFunction = new ModulusHashFunction(N);
        Hash hash = new ChainHash(hashFunction, N);
        for (int i =0; i <n;i++){
            int number1 = randomGenerator.uniformDistribution(universeSize);
            int number2 = randomGenerator.uniformDistribution(universeSize);
            TimedValue<Void> timedValue = hash.insert(new Pair(number1, number2));
            results.add(new Result(OperationType.INSERT, i, timedValue.getTimeTaken()));
            inserted.add(number1);
            if (i%searchSpace==0){
                try {
                    TimedValue<Pair> pair = hash.search(getInsertedRandom(randomGenerator, inserted));
                    results.add(new Result(OperationType.SEARCH_FOUND, i, timedValue.getTimeTaken()));
                    pair = hash.search(randomGenerator.uniformDistribution(universeSize));
                    results.add(new Result(OperationType.SEARCH_FOUND, i, timedValue.getTimeTaken()));
                } catch (NotFoundException e) {
                    results.add(new Result(OperationType.SEARCH_NOT_FOUND, i, e.getTimeTaken()));
                }
            }
        }
        System.out.println(results);


    }

}
