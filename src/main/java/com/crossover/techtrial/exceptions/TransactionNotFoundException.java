package com.crossover.techtrial.exceptions;

public class TransactionNotFoundException extends RuntimeException {

    private static final String TRANSACTION_NOT_FOUND = "Transaction not found";

    public TransactionNotFoundException() {
        super(TRANSACTION_NOT_FOUND);
    }
}
