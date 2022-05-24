package com.magenic.mobog.exercise4app.exceptions;

import org.springframework.http.HttpStatus;

public class UserExistsException extends RuntimeException {
    HttpStatus httpStatus;

    public UserExistsException(){
        this.httpStatus = HttpStatus.CONFLICT;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
