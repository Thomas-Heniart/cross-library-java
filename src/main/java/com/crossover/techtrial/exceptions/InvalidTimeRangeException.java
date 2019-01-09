package com.crossover.techtrial.exceptions;

public class InvalidTimeRangeException extends RuntimeException {

    private static final String INVALID_TIME_RANGE = "Invalid time range chosen";

    public InvalidTimeRangeException() {
        super(INVALID_TIME_RANGE);
    }
}
