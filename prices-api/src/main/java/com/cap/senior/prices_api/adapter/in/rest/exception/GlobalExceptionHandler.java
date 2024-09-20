package com.cap.senior.prices_api.adapter.in.rest.exception;


import com.cap.senior.prices_api.domain.exception.ServiceUnavailableException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerErrorException;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handle and map exceptions into the desired http error codes
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<SimpleErrorResponse> handleServiceUnavailable(ServiceUnavailableException ex) {
        SimpleErrorResponse errorResponse = SimpleErrorResponse.serviceUnavailable();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Map<String, String> handleIllegalArgumentException(IllegalArgumentException ex) {
//        Map<String, String> error = new HashMap<>();
//        error.put("error", ex.getMessage());
//        return error;
//    }


}