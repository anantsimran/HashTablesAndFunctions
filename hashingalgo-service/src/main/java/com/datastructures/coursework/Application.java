package com.datastructures.coursework;

import com.datastructures.coursework.algo.CuckooHash;
import com.datastructures.coursework.api.Hash;
import com.datastructures.coursework.api.HashFunctionGenerator;
import com.datastructures.coursework.api.Plotter;
import com.datastructures.coursework.api.Transformer;
import com.datastructures.coursework.exception.NotFoundException;
import com.datastructures.coursework.hashFunctionGenerator.KIndependentHashFunctionGenerator;
import com.datastructures.coursework.model.ActivityType;
import com.datastructures.coursework.model.Coordinate;
import com.datastructures.coursework.model.Pair;
import com.datastructures.coursework.model.TimedValue;
import com.datastructures.coursework.plotter.CoordinatePlotter;
import com.datastructures.coursework.utils.ApplicationUtils;
import com.datastructures.coursework.utils.HashingUtils;
import com.datastructures.coursework.utils.RandomGenerator;
import com.datastructures.coursework.utils.TimeCountUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Application {

    public static Integer getInsertedRandom(RandomGenerator randomGenerator, List<Integer> inserted){
        Objects.requireNonNull(inserted);
        if (inserted.isEmpty()){
            throw new RuntimeException("Inserted list is empty");
        }
        int number = randomGenerator.uniformDistribution(inserted.size());
        return inserted.get(number);
    }

    public static void main(String[] args){
        ApplicationUtils applicationUtils = new ApplicationUtils();

        int numberOfIterations = 100;
        int n = 2000;
        int universeSize = 100000000;
        double resizeAlpha = 0.1;


        List<Integer> primes = applicationUtils.getList();
        HashingUtils hashingUtils = new HashingUtils(primes, 999980);
        RandomGenerator randomGenerator = new RandomGenerator();
        TimeCountUtils timeCountUtils = new TimeCountUtils();



        HashFunctionGenerator hashFunctionGenerator = getHashFunctionGenerator(randomGenerator, hashingUtils);


        List<Coordinate> resultInsert = new ArrayList<>();
        List<Coordinate> resultInsertOperations = new ArrayList<>();
        List<Coordinate> resultInsertNoOfRehashes = new ArrayList<>();
        List<Coordinate> resultSearchFound = new ArrayList<>();
        List<Coordinate> resultSearchNotFound = new ArrayList<>();

        Transformer logTransformer = value -> Math.log(value);
        Transformer minusLogTransformer = value -> -Math.log(value);
        Transformer identityTransformer = value -> value;




        for (int trial = 0; trial < numberOfIterations; trial++) {
            Hash hash = new CuckooHash(universeSize,
                    hashingUtils,
                    hashFunctionGenerator,
                    timeCountUtils,
                    resizeAlpha
            );



            List<Integer> inserted = new ArrayList<>();
            for (int i =0; i <n;i++){
                int number1 = randomGenerator.uniformDistribution(universeSize);
                int number2 = randomGenerator.uniformDistribution(universeSize);
                TimedValue<Void> timedValue = hash.insert(new Pair(number1, number2));
                inserted.add(number1);

                double alpha = (double)i/hash.getCapacity();



                resultInsert.add(new Coordinate(alpha,(double) timedValue.getTimeCount().getTimeTaken()));
                resultInsertOperations.add(new Coordinate(alpha,(double) timedValue.getTimeCount().getOperationsPerformed()));
                resultInsertNoOfRehashes.add(new Coordinate(alpha,(double) timeCountUtils.getActivityTimeCount(timedValue.getTimeCount(), ActivityType.CUCKOO_REHASH).get().getOperationsPerformed()));

                try {
                    TimedValue<Pair> searchTimedValue = hash.search(getInsertedRandom(randomGenerator, inserted));
                    resultSearchFound.add(new Coordinate(alpha,(double) searchTimedValue.getTimeCount().getTimeTaken()));
                    searchTimedValue = hash.search(randomGenerator.uniformDistribution(universeSize));
                    resultSearchFound.add(new Coordinate(alpha,(double) searchTimedValue.getTimeCount().getTimeTaken()));
                } catch (NotFoundException e) {
                    resultSearchNotFound.add(new Coordinate(alpha,(double) e.getTimeTaken()));
                }

            }
        }



        String hashFunction = "Universal Hash Function";

        System.out.println("created HashTable");


        String funcType = "k_5";

        String chartTitle = funcType+ "Inserts.Timetaken";
        String chartTitleAmortised = funcType+ "Inserts.Timetaken.Amortised";
        String chartTitleOperationsAmortised = funcType+"_"+ "Inserts.Operations.Amortised";
        String chartTitleRehashes = funcType+"_"+ "Inserts.NoOfRehashes.Amortised";
        String xAxisTitle =  "alpha";
        String yAxisTitle = "time taken";



        Plotter tablePlotter = new CoordinatePlotter();


        tablePlotter.plot(resultInsert.iterator(),chartTitle,xAxisTitle,yAxisTitle,false,identityTransformer,identityTransformer);
        tablePlotter.plot(resultInsert.iterator(),chartTitleAmortised,xAxisTitle,yAxisTitle,true,identityTransformer,identityTransformer);
        tablePlotter.plot(resultInsertOperations.iterator(),chartTitleOperationsAmortised,xAxisTitle,yAxisTitle,true,identityTransformer,identityTransformer);
        tablePlotter.plot(resultInsertNoOfRehashes.iterator(),chartTitleRehashes,xAxisTitle,yAxisTitle,false,identityTransformer,identityTransformer);
    }

    private static HashFunctionGenerator getHashFunctionGenerator(RandomGenerator randomGenerator, HashingUtils hashingUtils) {
        return new KIndependentHashFunctionGenerator(randomGenerator, hashingUtils, 5);

    }

}
