package com.pba.authservice.controller.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pba.authservice.exceptions.AuthException;
import com.pba.authservice.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UserControllerExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthException.class)
    public ApiExceptionResponse handleAuthException(AuthException exception) {
        return new ApiExceptionResponse(exception.getHttpStatus(), ZonedDateTime.now(), Map.of(exception.getCode(), exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiExceptionResponse handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = this.getErrorMap(exception);
        return new ApiExceptionResponse(HttpStatus.BAD_REQUEST, ZonedDateTime.now(), errorMap);
    }

    private Map<String, String> getErrorMap(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
