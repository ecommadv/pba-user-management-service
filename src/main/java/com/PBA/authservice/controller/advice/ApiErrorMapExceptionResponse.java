package com.pba.authservice.controller.advice;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Map;

public record ApiErrorMapExceptionResponse(String message, HttpStatus httpStatus, ZonedDateTime timestamp, Map<String, String> errors) {}
