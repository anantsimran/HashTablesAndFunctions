package com.datastructures.coursework.algo;

import com.datastructures.coursework.api.Hash;
import com.datastructures.coursework.api.HashFunction;
import com.datastructures.coursework.api.HashFunctionGenerator;
import com.datastructures.coursework.exception.CuckooCycleException;
import com.datastructures.coursework.exception.NotFoundException;
import com.datastructures.coursework.model.ActivityType;
import com.datastructures.coursework.model.Pair;
import com.datastructures.coursework.model.TimeCount;
import com.datastructures.coursework.model.TimedValue;
import com.datastructures.coursework.utils.HashingUtils;
import com.datastructures.coursework.utils.TimeCountUtils;

import java.util.ArrayList;
import java.util.List;

public class CuckooHash implements Hash {
    private int maximumSize;
    private int universeSize;
    private HashFunctionGenerator hashFunctionGenerator;
    private HashFunction[] hashFunction;
    private HashingUtils hashingUtils;
    private int present;
    private TimeCountUtils timeCountUtils;
    private double resizeAlpha;

    private Pair[][] hashTable;

    private void initialise(int size){
        Pair[] table1 = new Pair[size];
        Pair[] table2 = new Pair[size];
        hashTable = new Pair[2][];
        hashTable[0]= table1;
        hashTable[1]= table2;
        HashFunction hashFunction1 = hashFunctionGenerator.generateNew(maximumSize,universeSize);
        HashFunction hashFunction2 = hashFunctionGenerator.generateNew(maximumSize,universeSize);
        hashFunction = new HashFunction[2];
        hashFunction[0]= hashFunction1;
        hashFunction[1]= hashFunction2;
    }

    private List<Pair> getList(){
        List<Pair> present = new ArrayList<>();
        for (int i = 0; i < maximumSize; i++) {
            if (hashTable[0][i]!=null){
                present.add(hashTable[0][i]);
            }
            if (hashTable[1][i]!=null){
                present.add(hashTable[1][i]);
            }
        }
        return present;
    }

    public CuckooHash(int universeSize, HashingUtils hashingUtils, HashFunctionGenerator hashFunctionGenerator, TimeCountUtils timeCountUtils, double resizeAlpha) {
        this.maximumSize=1019;
        this.universeSize = universeSize;
        this.hashingUtils = hashingUtils;
        this.hashFunctionGenerator = hashFunctionGenerator;
        this.timeCountUtils = timeCountUtils;
        this.resizeAlpha = resizeAlpha;
        initialise(maximumSize);

    }

    @Override
    public TimedValue<Pair> search(int key) throws NotFoundException {
        long startTime = System.nanoTime();
        long totalHashFunctionTime = 0L;
        TimedValue<Integer> hashFunctionOutput1 = hashFunction[0].getHash(key);
        int index1 = hashFunctionOutput1.getValue();
        totalHashFunctionTime += hashFunctionOutput1.getTimeCount().getTimeTaken();
        if(hashTable[0][index1]!=null && hashTable[0][index1].getKey().equals(key)){
            return new TimedValue<Pair>(hashTable[0][index1],
                    new TimeCount(
                    System.nanoTime()- startTime,
                    1 , ActivityType.HASH));
        }
        TimedValue<Integer> hashFunctionOutput2 = hashFunction[1].getHash(key);
        int index2 = hashFunctionOutput2.getValue();
        totalHashFunctionTime += hashFunctionOutput2.getTimeCount().getTimeTaken();

        if(hashTable[1][index2]!=null && hashTable[1][index2].getKey().equals(key)){
            TimedValue<Pair> timedValue = new TimedValue<>(hashTable[0][index2],
                    new TimeCount(
                    System.nanoTime() - startTime,
                    2, ActivityType.HASH));
            timedValue.getTimeCount().addSubTime (new TimeCount(totalHashFunctionTime,null, ActivityType.HASH_FUNCTION));
            return timedValue;
        }
        throw new NotFoundException("Key: "+ key + " Not found in hash table", System.nanoTime()- startTime);
    }

    private TimeCount rehashTable(int maximumSize){
        List<Pair> inserted = this.getList();
        this.maximumSize= maximumSize;
        this.present=0;

        long hashingTimeTaken = 0L;
        int operationsPerformed =0;
        int noOfRehashes=1;

        initialise(maximumSize);
        operationsPerformed++;

        for(Pair p : inserted){
            TimedValue<Void> insertTimedValue= insert(p);
            hashingTimeTaken += timeCountUtils.getActivityTimeCount(insertTimedValue.getTimeCount(), ActivityType.HASH_FUNCTION).get()
                    .getTimeTaken();
            noOfRehashes += timeCountUtils.getActivityTimeCount(insertTimedValue.getTimeCount(), ActivityType.CUCKOO_REHASH).get()
                    .getOperationsPerformed();
            operationsPerformed+= insertTimedValue.getTimeCount().getOperationsPerformed();
        }
        TimeCount hashFunctionTimeCount = new TimeCount(hashingTimeTaken,null, ActivityType.HASH_FUNCTION);
        TimeCount rehashTimeCount = new TimeCount(null, noOfRehashes, ActivityType.CUCKOO_REHASH);
        TimeCount timeCount = new TimeCount(null, operationsPerformed, ActivityType.HASH);
        timeCount.addSubTime(hashFunctionTimeCount);
        timeCount.addSubTime(rehashTimeCount);
        return timeCount;
    }

    private TimeCount insert(Pair pair,  int hashtableIndex, Pair initialPair, int count) throws CuckooCycleException {
        if(initialPair.getKey().equals(pair.getKey()) && hashtableIndex==0){
            if (count==2){
                throw new CuckooCycleException();
            }
            else {
                count++;
            }
        }
        TimedValue<Integer> hashFunctionOutput = hashFunction[hashtableIndex].getHash(pair.getKey());
        int index = hashFunctionOutput.getValue();
        if (hashTable[hashtableIndex][index]==null){
            hashTable[hashtableIndex][index] = pair;
            TimeCount timeCount = new TimeCount(null, 1, ActivityType.HASH);
            timeCount.addSubTime(
                    new TimeCount(hashFunctionOutput.getTimeCount().getTimeTaken(),null, ActivityType.HASH_FUNCTION));
            return timeCount;
        }
        Pair oldPair= hashTable[hashtableIndex][index];
        hashTable[hashtableIndex][index] = pair;
        TimeCount recursionTimeCount =  insert(oldPair,1-hashtableIndex,initialPair,count);
        TimeCount timeCount = new TimeCount(null, 1+ recursionTimeCount.getOperationsPerformed(), ActivityType.HASH);
        timeCount.addSubTime(
                new TimeCount(
                        hashFunctionOutput.getTimeCount().getTimeTaken() +
                                timeCountUtils.getActivityTimeCount(recursionTimeCount, ActivityType.HASH_FUNCTION).get().getTimeTaken(),
                        null,
                        ActivityType.HASH_FUNCTION));
        return timeCount;
    }


    @Override
    public TimedValue<Void> insert(Pair pair) {
        long startTime = System.nanoTime();
        long hashFunctionTimeTaken =0L;
        int operationsPerformed =0;
        int noOfRehashes =0;

        TimedValue<Integer> hashFunctionOutput1 = hashFunction[0].getHash(pair.getKey());
        int index1 = hashFunctionOutput1.getValue();
        operationsPerformed++;
        hashFunctionTimeTaken+=hashFunctionOutput1.getTimeCount().getTimeTaken();

        if(hashTable[0][index1]!=null && hashTable[0][index1].getKey().equals(pair.getKey())){
            hashTable[0][index1].setValue(pair.getValue());
            return getInsertTimedValue(startTime, hashFunctionTimeTaken, operationsPerformed, noOfRehashes);
        }
        TimedValue<Integer> hashFunctionOutput2 = hashFunction[1].getHash(pair.getKey());
        int index2 = hashFunctionOutput2.getValue();
        operationsPerformed++;
        hashFunctionTimeTaken+=hashFunctionOutput1.getTimeCount().getTimeTaken();

        if(hashTable[1][index2]!=null && hashTable[1][index2].getKey().equals(pair.getKey())){
            hashTable[1][index2].setValue(pair.getValue());
            return getInsertTimedValue(startTime, hashFunctionTimeTaken, operationsPerformed, noOfRehashes);
        }



        try {
            TimeCount insertTimeCount = insert(pair, 0, pair, 0);
            hashFunctionTimeTaken += timeCountUtils.getActivityTimeCount(insertTimeCount, ActivityType.HASH_FUNCTION).get().getTimeTaken() ;
            operationsPerformed+= insertTimeCount.getOperationsPerformed();
        } catch (CuckooCycleException e) {
            TimeCount rehashTimeCount =  rehashTable(maximumSize);
//            TimeCount repeatInsertTimeCount = insert(pair).getTimeCount();

            operationsPerformed+= rehashTimeCount.getOperationsPerformed();
            noOfRehashes+=timeCountUtils.getActivityTimeCount(rehashTimeCount, ActivityType.CUCKOO_REHASH).get().getOperationsPerformed();
            hashFunctionTimeTaken+=timeCountUtils.getActivityTimeCount(rehashTimeCount, ActivityType.HASH_FUNCTION).get().getTimeTaken();

//            operationsPerformed+= repeatInsertTimeCount.getOperationsPerformed();
//            noOfRehashes+=timeCountUtils.getActivityTimeCount(repeatInsertTimeCount, ActivityType.CUCKOO_REHASH).get().getOperationsPerformed();
//            hashFunctionTimeTaken+=timeCountUtils.getActivityTimeCount(repeatInsertTimeCount, ActivityType.HASH_FUNCTION).get().getTimeTaken();
        }
        present++;
        if (present>maximumSize* resizeAlpha){
            int newMaximumSize = hashingUtils.getNextPrime(maximumSize*2);
            TimeCount rehashTimeCount =  rehashTable(newMaximumSize);
            operationsPerformed+= rehashTimeCount.getOperationsPerformed();
            noOfRehashes+=timeCountUtils.getActivityTimeCount(rehashTimeCount, ActivityType.CUCKOO_REHASH).get().getOperationsPerformed();
            hashFunctionTimeTaken+=timeCountUtils.getActivityTimeCount(rehashTimeCount, ActivityType.HASH_FUNCTION).get().getTimeTaken();
        }

        return getInsertTimedValue(startTime, hashFunctionTimeTaken, operationsPerformed, noOfRehashes);

    }

    private TimedValue<Void> getInsertTimedValue(long startTime, long hashFunctionTimeTaken, int operationsPerformed, int noOfRehashes) {
        TimedValue<Void> timedValue = new TimedValue<>(null,
                new TimeCount(
                        System.nanoTime() - startTime, operationsPerformed, ActivityType.HASH));
        timedValue.getTimeCount().addSubTime(new TimeCount(hashFunctionTimeTaken, null, ActivityType.HASH_FUNCTION));
        timedValue.getTimeCount().addSubTime(new TimeCount(null, noOfRehashes, ActivityType.CUCKOO_REHASH));
        return timedValue;
    }

    @Override
    public TimedValue<Boolean> delete(int key) {
        //TODO Anant
        throw new UnsupportedOperationException("Delete is not supported in this version");
    }

    @Override
    public double getCapacity() {
        return this.maximumSize;
    }
}
