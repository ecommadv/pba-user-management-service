package com.pba.authservice.exceptions;

public class AuthServiceException extends RuntimeException {
    public AuthServiceException(String message) {
        super(message);
    }
}
