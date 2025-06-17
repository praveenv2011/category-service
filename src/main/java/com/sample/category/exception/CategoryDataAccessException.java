package com.sample.category.exception;

import org.springframework.http.HttpStatus;

public class CategoryDataAccessException extends RuntimeException {
    private final HttpStatus status;
    public CategoryDataAccessException(String string, HttpStatus httpStatus) {
        super(string);
        this.status = httpStatus;
    }

    HttpStatus getStatus(){
        return status;
    }
}
