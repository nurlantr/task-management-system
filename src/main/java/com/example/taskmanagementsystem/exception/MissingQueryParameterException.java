package com.example.taskmanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingQueryParameterException extends RuntimeException {
    public MissingQueryParameterException(String message) {
        super(message);
    }
}
