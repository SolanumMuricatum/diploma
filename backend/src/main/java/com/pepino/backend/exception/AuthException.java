package com.pepino.backend.exception;

public class AuthException extends IllegalArgumentException{
    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
