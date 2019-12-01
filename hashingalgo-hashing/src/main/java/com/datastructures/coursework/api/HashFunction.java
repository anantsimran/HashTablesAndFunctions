package com.datastructures.coursework.api;

import com.datastructures.coursework.model.TimedValue;

public interface HashFunction {
    public TimedValue<Integer> getHash(int input);
}
