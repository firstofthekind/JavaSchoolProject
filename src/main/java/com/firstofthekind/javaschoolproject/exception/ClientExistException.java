package com.firstofthekind.javaschoolproject.exception;

public class ClientExistException extends RuntimeException {
    public ClientExistException(final String message) {
        super(message);
    }
}
