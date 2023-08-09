package com.pba.authservice.exceptions;

public class AuthDaoException extends RuntimeException {
    public AuthDaoException(String message) {
        super(message);
    }
}
