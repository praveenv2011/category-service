package com.sample.category.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends RuntimeException {

    private final HttpStatus status;
    public CategoryNotFoundException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
