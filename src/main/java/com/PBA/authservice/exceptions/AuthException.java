package com.pba.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {
    private String code;
    private HttpStatus httpStatus;

    public AuthException(String code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
