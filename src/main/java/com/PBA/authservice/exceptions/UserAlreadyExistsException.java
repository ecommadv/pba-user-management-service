package com.pba.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }
}
