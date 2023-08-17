package com.pba.authservice.exceptions;

import java.util.Map;

public class AuthValidationException extends RuntimeException {
    private Map<String, String> errorMap;

    public AuthValidationException(String message) {
        super(message);
    }

    public AuthValidationException(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }
}
