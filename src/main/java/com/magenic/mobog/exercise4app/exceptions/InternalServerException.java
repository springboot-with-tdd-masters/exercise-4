package com.magenic.mobog.exercise4app.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerException extends RuntimeException{    HttpStatus httpStatus;

    public InternalServerException(){
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
