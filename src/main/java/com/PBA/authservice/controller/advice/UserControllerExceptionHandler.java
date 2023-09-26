package com.pba.authservice.controller.advice;

import com.pba.authservice.exceptions.AuthException;
import com.pba.authservice.exceptions.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UserControllerExceptionHandler {
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiExceptionResponse> handleAuthException(AuthException exception) {
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(ZonedDateTime.now(), Map.of(exception.getCode(), exception.getMessage()));
        return new ResponseEntity<>(apiExceptionResponse, exception.getHttpStatus());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiExceptionResponse handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = this.getErrorMap(exception);
        return new ApiExceptionResponse(ZonedDateTime.now(), errorMap);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Void> handleAuthorizationException(AuthorizationException exception) {
        return new ResponseEntity<>(exception.getHttpStatus());
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
