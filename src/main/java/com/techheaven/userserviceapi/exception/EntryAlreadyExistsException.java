package com.techheaven.userserviceapi.exception;

public class EntryAlreadyExistsException extends RuntimeException{
    public EntryAlreadyExistsException(String message) {
        super(message);
    }
}
