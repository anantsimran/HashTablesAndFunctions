package com.datastructures.coursework.algo;

import com.datastructures.coursework.api.Hash;
import com.datastructures.coursework.api.HashFunction;
import com.datastructures.coursework.exception.NotFoundException;
import com.datastructures.coursework.model.ActivityType;
import com.datastructures.coursework.model.Pair;
import com.datastructures.coursework.model.TimeCount;
import com.datastructures.coursework.model.TimedValue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public class ChainHash implements Hash<Void> {
    private HashFunction hashFunction;
    private int size;

    private LinkedList<Pair>[] hashTable;

    public ChainHash(HashFunction hashFunction, int size) {
        this.hashFunction = hashFunction;
        this.size = size;
        hashTable = new LinkedList[size];
        for (int i = 0; i < size; i++) {
            hashTable[i]= new LinkedList<>();
        }
    }

    @Override
    public TimedValue<Pair> search(int key) throws NotFoundException {
        long startTime = System.nanoTime();
        int index = hashFunction.getHash(key).getValue();
        if (index>size){
            throw new RuntimeException("Index bigger than size of hashTable");
        }
        LinkedList<Pair> indexList = hashTable[index];
        if (indexList==null || indexList.isEmpty()){
            throw new NotFoundException("Key: "+ key + " not found in HashTable",System.nanoTime()-startTime );
        }
        int listIndex =0;
        for(Pair p: indexList){
            listIndex++;
            if (p.getKey().equals(key)){
                return new TimedValue<Pair>(p, new TimeCount(System.nanoTime()-startTime, listIndex+1, ActivityType.HASH));
            }
        }
        throw new NotFoundException("Key: "+ key + " not found in HashTable",System.nanoTime()-startTime);
    }

    @Override
    public TimedValue<Void> insert(Pair pair) {
        long startTime = System.nanoTime();
        Objects.requireNonNull(pair);
        int index = hashFunction.getHash(pair.getKey()).getValue();
        if (index>size){
            throw new RuntimeException("Index bigger than size of hashTable");
        }
        LinkedList<Pair> indexList = hashTable[index];
        boolean found = false;
        for(Pair p : indexList){
            if (p.getKey().equals(pair.getKey())){
                p.setValue(pair.getValue());
                found = true;
                break;
            }
        }
        if (!found) {
            indexList.addFirst(pair);
        }
        return new TimedValue<>(null,new TimeCount(System.nanoTime()-startTime, indexList.size(),ActivityType.HASH));
    }

    @Override
    public TimedValue<Boolean> delete(int key) {
        long startTime = System.nanoTime();
        int index = hashFunction.getHash(key).getValue();
        if (index>size){
            throw new RuntimeException("Index bigger than size of hashTable");
        }
        LinkedList<Pair> indexList = hashTable[index];
        if (indexList==null || indexList.isEmpty()){
            return new TimedValue<>(false, new TimeCount(System.nanoTime()-startTime, 1, ActivityType.HASH));
        }
        int listIndex = 0;
        Iterator<Pair> iterator = indexList.listIterator();
        while (iterator.hasNext())
        {
            listIndex++;
            Pair p = iterator.next();
            if(p.getKey().equals(key)){
                iterator.remove();
                return new TimedValue<>(true, new TimeCount(System.nanoTime()-startTime, listIndex+1, ActivityType.HASH));
            }
        }
        return new TimedValue<>(false, new TimeCount(System.nanoTime()-startTime, listIndex+1, ActivityType.HASH));
    }

    @Override
    public double getCapacity() {
        return this.size;
    }
}
