package com.brownbag.exercise4.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    protected ResponseEntity<Object> handleBookNotFoundException() {
        return new ResponseEntity<>("Book Not Found.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    protected ResponseEntity<Object> handleAuthorNotFoundException() {
        return new ResponseEntity<>("Author Not Found.", HttpStatus.NOT_FOUND);
    }
}
