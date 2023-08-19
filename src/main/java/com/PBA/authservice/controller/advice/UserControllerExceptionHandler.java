package com.pba.authservice.controller.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pba.authservice.exceptions.UserNotFoundException;
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
    @ExceptionHandler(UserNotFoundException.class)
    public ApiExceptionResponse handleUserNotFoundException(UserNotFoundException exception) {
        return new ApiExceptionResponse(exception.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now(), exception.getErrorMap());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiExceptionResponse handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = this.getErrorMap(exception);
        String message = "Error at validation";
        return new ApiExceptionResponse(message, HttpStatus.BAD_REQUEST, ZonedDateTime.now(), errorMap);
    }

    private Map<String, String> getErrorMap(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
