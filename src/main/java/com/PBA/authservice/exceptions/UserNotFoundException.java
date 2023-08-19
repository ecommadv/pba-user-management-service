package com.pba.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String code, String message) {
        super(code, message, HttpStatus.NOT_FOUND);
    }
}
