package com.pba.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends AuthException {
    public EntityNotFoundException(String code, String message) {
        super(code, message, HttpStatus.NOT_FOUND);
    }
}
