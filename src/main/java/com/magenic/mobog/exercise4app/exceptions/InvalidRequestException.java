package com.magenic.mobog.exercise4app.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends RuntimeException {

    HttpStatus httpStatus;

    public InvalidRequestException(){
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
