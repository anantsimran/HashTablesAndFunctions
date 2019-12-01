package com.datastructures.coursework;

import com.datastructures.coursework.algo.ChainHash;
import com.datastructures.coursework.api.Hash;
import com.datastructures.coursework.api.HashFunction;
import com.datastructures.coursework.exception.NotFoundException;
import com.datastructures.coursework.hashFunction.ModulusHashFunction;
import com.datastructures.coursework.model.TimedValue;
import com.datastructures.coursework.plotter.HashTablePlotter;
import com.datastructures.coursework.utils.ApplicationUtils;
import com.datastructures.coursework.utils.HashingUtils;
import com.datastructures.coursework.utils.RandomGenerator;
import com.datastructures.coursework.model.Pair;

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
        ApplicationUtils applicationUtils = new ApplicationUtils();
        int n = 200000;
        int N = 500;
        int universeSize = 100000000;
        List<Integer> primes = applicationUtils.getList();
        HashingUtils hashingUtils = new HashingUtils(primes, 999980);
        RandomGenerator randomGenerator = new RandomGenerator();


        HashFunction hashFunction = new ModulusHashFunction(N);

        Hashtable<Double, Result> resultInsert = new Hashtable<>();
        Hashtable<Double, Result> resultSearchFound = new Hashtable<>();
        Hashtable<Double, Result> resultSearchNotFound = new Hashtable<>();

        int numberOfTrials = 20;

        for (int j = 0; j < numberOfTrials; j++) {
            Hash hash = new ChainHash(hashFunction, N);
            List<Integer> inserted = new ArrayList<>();
            for (int i =0; i <n;i++){
                int number1 = randomGenerator.uniformDistribution(universeSize);
                int number2 = randomGenerator.uniformDistribution(universeSize);
                TimedValue<Void> timedValue = hash.insert(new Pair(number1, number2));
                inserted.add(number1);
                if(i>1000 && i%300==0 ) {
                    double alpha = (double)i/hash.getCapacity()+1;
                    insertInHashTable(resultInsert,alpha,timedValue.getTimeTaken());
                    try {
                        TimedValue<Pair> search = hash.search(getInsertedRandom(randomGenerator, inserted));
                        insertInHashTable(resultSearchFound,alpha,search.getTimeTaken());
                        search = hash.search(randomGenerator.uniformDistribution(universeSize));
                        insertInHashTable(resultSearchFound,alpha,search.getTimeTaken());
                    } catch (NotFoundException e) {
                        insertInHashTable(resultSearchNotFound,alpha,e.getTimeTaken());
                    }
                }
            }
        }



        System.out.println("created HashTable");

        HashTablePlotter tablePlotter = new HashTablePlotter();
        tablePlotter.plot(resultInsert);


    }

    private static  void insertInHashTable(Hashtable<Double, Result> table, Double i, long timeTaken) {
        Result result = table.get(i);
        if (result ==null){
            table.put(i, new Result(timeTaken, 1));
            return;
        }
        result.setTotal(result.getTotal()+1);
    }

}
