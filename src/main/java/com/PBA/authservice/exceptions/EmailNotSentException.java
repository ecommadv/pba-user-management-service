package com.pba.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class EmailNotSentException extends AuthException {
    public EmailNotSentException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }
}
