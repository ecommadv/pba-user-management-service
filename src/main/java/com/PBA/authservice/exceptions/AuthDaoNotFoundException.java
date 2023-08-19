package com.pba.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class AuthDaoNotFoundException extends AuthDaoException {
    public AuthDaoNotFoundException(String code, String message) {
        super(code, message, HttpStatus.NOT_FOUND);
    }
}
