package com.pepino.authservice.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        String error,
        int status,
        String timestamp
) {
    public ErrorResponse(String error, HttpStatus status) {
        this(error, status.value(), LocalDateTime.now().toString());
    }
}
