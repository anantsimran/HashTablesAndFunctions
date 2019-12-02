package com.datastructures.coursework.api;

import com.datastructures.coursework.exception.NotFoundException;
import com.datastructures.coursework.model.Pair;
import com.datastructures.coursework.model.TimedValue;

public interface Hash<R> {
    public TimedValue<Pair> search(int key) throws NotFoundException;

    public TimedValue<Void> insert(Pair pair);

    public TimedValue<Boolean> delete(int key);

    public double getCapacity();
}
