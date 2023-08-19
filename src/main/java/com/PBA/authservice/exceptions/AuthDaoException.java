package com.pba.authservice.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class AuthDaoException extends AuthException {
    public AuthDaoException(String code, String message, HttpStatus httpStatus) {
        super(code, message, httpStatus);
    }
}
