package com.firstofthekind.javaschoolproject.exception;

public class ClientExistException extends RuntimeException {
    public String clientExistException() {
        return "Error. Client already exist.";
    }
}
