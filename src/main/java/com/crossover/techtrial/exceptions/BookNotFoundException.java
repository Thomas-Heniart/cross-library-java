package com.crossover.techtrial.exceptions;

public class BookNotFoundException extends RuntimeException {

    private static final String BOOK_NOT_FOUND = "Book not found";

    public BookNotFoundException() {
        super(BOOK_NOT_FOUND);
    }
}
