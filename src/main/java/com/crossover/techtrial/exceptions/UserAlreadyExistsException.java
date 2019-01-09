package com.crossover.techtrial.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    private static final String USER_ALREADY_EXISTS = "This user already exists";

    public UserAlreadyExistsException() {
        super(USER_ALREADY_EXISTS);
    }
}
