package com.magenic.mobog.exercise3app.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends RuntimeException{

    HttpStatus httpStatus;

    public EntityNotFoundException(){
        this.httpStatus = HttpStatus.NOT_FOUND;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
