package com.sample.category.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends Exception {
    private final HttpStatus httpStatus;
    public ApplicationException(String string, HttpStatus httpStatus) {
        super(string);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
