package com.datastructures.coursework.exception;

public class NotFoundException extends Exception {
    private long timeTaken;

    public long getTimeTaken() {
        return timeTaken;
    }

    public NotFoundException(String message, long timeTaken) {
        super(message);
        this.timeTaken = timeTaken;
    }
}
