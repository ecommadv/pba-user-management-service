package com.pba.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends AuthException {
    public AuthorizationException() {
        super("", "", HttpStatus.UNAUTHORIZED);
    }
}
