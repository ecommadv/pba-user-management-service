package com.pba.authservice.exceptions;

import java.util.Map;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String message, Map<String, String> errorMap) {
        super(message, errorMap);
    }
}
