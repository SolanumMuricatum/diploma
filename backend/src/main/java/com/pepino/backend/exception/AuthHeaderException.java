package com.pepino.backend.exception;

public class AuthHeaderException extends IllegalArgumentException{
    public AuthHeaderException(String message) {
        super(message);
    }

    public AuthHeaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
