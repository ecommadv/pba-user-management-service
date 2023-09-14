package com.pba.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class EntityAlreadyExistsException extends AuthException {
    public EntityAlreadyExistsException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }
}
