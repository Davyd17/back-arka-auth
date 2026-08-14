package com.arka.exceptions;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(){
        super("The provided token is invalid or has expired.");
    }
}
