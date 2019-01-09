package com.crossover.techtrial.exceptions;

public class TooManyBooksIssuedException extends RuntimeException {

    private static final String TOO_MANY_BOOKS_ISSUED = "Too many books issued";

    public TooManyBooksIssuedException() {
        super(TOO_MANY_BOOKS_ISSUED);
    }
}
