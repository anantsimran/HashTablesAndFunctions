package com.anantsimran.coursework.algo;

import com.anantsimran.coursework.api.Hash;
import com.anantsimran.coursework.api.HashFunction;
import com.anantsimran.coursework.api.HashFunctionGenerator;
import com.anantsimran.coursework.exception.CuckooCycleException;
import com.anantsimran.coursework.exception.NotFoundException;
import com.anantsimran.coursework.model.Pair;
import com.anantsimran.coursework.model.TimedValue;
import com.anantsimran.coursework.utils.HashingUtils;

import java.util.ArrayList;
import java.util.List;

public class CuckooHash implements Hash {
    private int maximumSize;
    private int universeSize;
    private HashFunctionGenerator hashFunctionGenerator;
    private HashFunction[] hashFunction;
    private HashingUtils hashingUtils;
    private int present;

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

    public CuckooHash( int universeSize, HashingUtils hashingUtils, HashFunctionGenerator hashFunctionGenerator) {
        this.maximumSize=1019;
        this.universeSize = universeSize;
        this.hashingUtils = hashingUtils;
        this.hashFunctionGenerator = hashFunctionGenerator;
        initialise(maximumSize);
    }

    @Override
    public TimedValue<Pair> search(int key) throws NotFoundException {
        long startTime = System.nanoTime();
        int index1 = hashFunction[0].getHash(key);
        if(hashTable[0][index1]!=null && hashTable[0][index1].getKey().equals(key)){
            return new TimedValue<>(hashTable[0][index1],System.nanoTime()- startTime);
        }
        int index2 = hashFunction[1].getHash(key);
        if(hashTable[1][index2]!=null && hashTable[1][index2].getKey().equals(key)){
            return new TimedValue<>(hashTable[1][index2],System.nanoTime()- startTime);
        }
        throw new NotFoundException("Key: "+ key + " Not found in hash table", System.nanoTime()- startTime);
    }

    private void rehashTable(int maximumSize){
        List<Pair> inserted = this.getList();
        this.maximumSize= maximumSize;
        this.present=0;
        initialise(maximumSize);
        for(Pair p : inserted){
            insert(p);
        }
    }

    private void insert(Pair pair,  int hashtableIndex, Pair initialPair, int count) throws CuckooCycleException {
        if(initialPair.getKey().equals(pair.getKey()) && hashtableIndex==0){
            if (count==2){
                throw new CuckooCycleException();
            }
            else {
                count++;
            }
        }
        int index = hashFunction[hashtableIndex].getHash(pair.getKey());
        if (hashTable[hashtableIndex][index]==null){
            hashTable[hashtableIndex][index] = pair;
            return ;
        }
        Pair oldPair= hashTable[hashtableIndex][index];
        hashTable[hashtableIndex][index] = pair;
        insert(oldPair,1-hashtableIndex,initialPair,count);
    }


    @Override
    public TimedValue<Void> insert(Pair pair) {
        long startTime = System.nanoTime();
        int index1 = hashFunction[0].getHash(pair.getKey());
        if(hashTable[0][index1]!=null && hashTable[0][index1].getKey().equals(pair.getKey())){
            hashTable[0][index1].setValue(pair.getValue());
            return new TimedValue<>(null,System.nanoTime()- startTime);
        }
        int index2 = hashFunction[1].getHash(pair.getKey());
        if(hashTable[1][index2]!=null && hashTable[1][index2].getKey().equals(pair.getKey())){
            hashTable[1][index2].setValue(pair.getValue());
            return new TimedValue<>(null,System.nanoTime()- startTime);
        }
        try {
            insert(pair, 0, pair, 0);
        } catch (CuckooCycleException e) {
            rehashTable(maximumSize);
            insert(pair);
        }
        present++;
        if (present>maximumSize){
            int newMaximumSize = hashingUtils.getNextPrime(maximumSize*2);
            rehashTable(newMaximumSize);
        }
        return new TimedValue<>(null,System.nanoTime()- startTime);

    }

    @Override
    public TimedValue<Boolean> delete(int key) {
        throw new UnsupportedOperationException("Delete is not supported in this version");
    }

    @Override
    public double getCapacity() {
        return this.maximumSize;
    }
}
