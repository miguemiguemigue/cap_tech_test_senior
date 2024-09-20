package com.cap.senior.prices_api.adapter.out.database.mongo.exception;

import com.cap.senior.prices_api.domain.exception.ServiceUnavailableException;

public class MongoConnectionException extends ServiceUnavailableException {

    public MongoConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
