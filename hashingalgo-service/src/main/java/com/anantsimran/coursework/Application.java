package com.anantsimran.coursework;

import com.anantsimran.coursework.algo.ChainHash;
import com.anantsimran.coursework.algo.CuckooHash;
import com.anantsimran.coursework.api.Hash;
import com.anantsimran.coursework.api.HashFunction;
import com.anantsimran.coursework.api.HashFunctionGenerator;
import com.anantsimran.coursework.exception.NotFoundException;
import com.anantsimran.coursework.hashFunction.ModulusHashFunction;
import com.anantsimran.coursework.hashFunction.UniversalHashFunctionGenerator;
import com.anantsimran.coursework.model.Average;
import com.anantsimran.coursework.model.OperationType;
import com.anantsimran.coursework.model.Pair;
import com.anantsimran.coursework.model.TimedValue;
import com.anantsimran.coursework.plotter.HashTablePlotter;
import com.anantsimran.coursework.utils.ApplicationUtils;
import com.anantsimran.coursework.utils.HashingUtils;
import com.anantsimran.coursework.utils.RandomGenerator;

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
        int maxN = 50000;
        int universeSize = 100000000;
        List<Integer> primes = applicationUtils.getList();
        HashingUtils hashingUtils = new HashingUtils(primes, 999980);
        RandomGenerator randomGenerator = new RandomGenerator();


        HashFunctionGenerator hashFunctionGenerator = new UniversalHashFunctionGenerator( randomGenerator, hashingUtils );
        Hash hash = new CuckooHash(universeSize,hashingUtils,hashFunctionGenerator);


        plotDataPoints(maxN, universeSize, randomGenerator, hash);



    }

    private static void plotDataPoints(int n, int universeSize, RandomGenerator randomGenerator, Hash hash) {
        List<Integer> inserted = new ArrayList<>();
        Hashtable<Integer, Average> nTimeSearchFound = new Hashtable<>();
        Hashtable<Integer, Average> nTimeInsert = new Hashtable<>();
        Hashtable<Integer, Average> nTimeSearchNotFound = new Hashtable<>();
        Hashtable<Double, Average> alphaInsert = new Hashtable<>();
        Hashtable<Double, Average> alphaSearchFound = new Hashtable<>();
        Hashtable<Double, Average> alphaSearchNotFound = new Hashtable<>();

        for (int i =0; i <n;i+=30){
            int number1 = randomGenerator.uniformDistribution(universeSize);
            int number2 = randomGenerator.uniformDistribution(universeSize);
            TimedValue<Void> timedValue = hash.insert(new Pair(number1, number2));
            Double alpha = (double)i/hash.getCapacity();
            inserted.add(number1);
            if(i>1000) {
                long timeTaken = timedValue.getTimeTaken();
                insertInHashTable(nTimeInsert, i, timeTaken);
                insertInHashTable(alphaSearchFound, alpha, timeTaken);
                try {
                    TimedValue<Pair> search = hash.search(getInsertedRandom(randomGenerator, inserted));
                    insertInHashTable(nTimeSearchFound, i, search.getTimeTaken());
                    insertInHashTable(alphaSearchFound, alpha, search.getTimeTaken());

                    search = hash.search(randomGenerator.uniformDistribution(universeSize));
                    insertInHashTable(nTimeSearchFound, i, search.getTimeTaken());
                    insertInHashTable(alphaSearchFound, alpha, search.getTimeTaken());

                } catch (NotFoundException e) {
                    insertInHashTable(nTimeSearchNotFound, i, e.getTimeTaken());
                    insertInHashTable(alphaSearchNotFound, alpha, e.getTimeTaken());

                }
            }
        }


        HashTablePlotter tablePlotter = new HashTablePlotter();
        tablePlotter.plot1(nTimeInsert, "Inserts");
    }

    private static <R>  void insertInHashTable(Hashtable<R, Average> nTimeInsert, R i, long timeTaken) {
        if (nTimeInsert.get(i)==null){
            nTimeInsert.put(i, new Average(1,timeTaken));
        }
        nTimeInsert.get(i).setTotal(nTimeInsert.get(i).getTotal()+1);
        nTimeInsert.get(i).setSum(nTimeInsert.get(i).getSum()+1);
    }

}
