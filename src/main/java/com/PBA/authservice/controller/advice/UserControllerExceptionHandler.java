package com.pba.authservice.controller.advice;

import com.pba.authservice.exceptions.AuthValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UserControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthValidationException.class)
    public Map<String, String> handleAuthValidationException(AuthValidationException exception) {
        Map<String, String> errorMap = exception.getErrorMap();
        errorMap.put("statusCode", "400");
        errorMap.put("error", "Bad Request");
        return errorMap;
    }
}
