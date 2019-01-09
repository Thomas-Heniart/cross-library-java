package com.crossover.techtrial.exceptions;

public class BookAlreadyIssuedException extends RuntimeException {

    private static final String BOOK_ALREADY_ISSUED = "Book already issued";

    public BookAlreadyIssuedException() {
        super(BOOK_ALREADY_ISSUED);
    }
}
