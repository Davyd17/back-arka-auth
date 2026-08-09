package com.arka.exception;

import java.time.Instant;

public record ErrorResponse(
        String status,
        String message,
        Instant timestamp
) {
    public static ErrorResponse of(String status, String message) {
        return new ErrorResponse(status, message, Instant.now());
    }
}
