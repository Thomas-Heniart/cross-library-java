package com.crossover.techtrial.exceptions;

public class BookAlreadyReturnedException extends RuntimeException {

    private static final String BOOK_ALREADY_RETURNED = "Book already returned";

    public BookAlreadyReturnedException() {
        super(BOOK_ALREADY_RETURNED);
    }
}
