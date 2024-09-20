package com.cap.senior.prices_api.adapter.in.rest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SimpleErrorResponse {
    private final String message;

    public String getMessage() {
        return message;
    }

    public static SimpleErrorResponse serviceUnavailable() {
        return new SimpleErrorResponse("Service is currently unavailable.");
    }
}