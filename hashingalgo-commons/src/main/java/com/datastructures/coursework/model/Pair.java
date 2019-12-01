package com.datastructures.coursework.model;

public class Pair {
    private Integer key;
    private Integer value;

    public Pair(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
