package com.pba.authservice.exceptions;

import java.util.Map;

public class AuthException extends RuntimeException {
    private Map<String, String> errorMap;

    public AuthException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

}
