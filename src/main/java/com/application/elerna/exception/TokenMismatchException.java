package com.application.elerna.exception;

import lombok.Getter;

@Getter
public class TokenMismatchException extends RuntimeException {

    private String accessUsername;
    private String refreshUserName;

    public TokenMismatchException(String message) {
        super(message);
    }

    public TokenMismatchException(String message, String accessUsername, String refreshUserName) {
        super(message);
        this.accessUsername = accessUsername;
        this.refreshUserName = refreshUserName;
    }
}
