package com.magenic.mobog.exercise4app.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends RuntimeException{
    HttpStatus httpStatus;

    public UserNotAllowedException(String message){
        super(message);
        this.httpStatus = HttpStatus.FORBIDDEN;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
