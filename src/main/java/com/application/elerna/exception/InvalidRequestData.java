package com.application.elerna.exception;

public class InvalidRequestData extends RuntimeException {
    public InvalidRequestData(String message) {
        super(message);
    }
}
