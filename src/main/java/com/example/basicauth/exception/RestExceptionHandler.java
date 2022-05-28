package com.example.basicauth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<String> handleAllException(Exception e, WebRequest req) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(AuthorNotFoundException.class)
	public final ResponseEntity<String> handleRecordNotFoundException(Exception e, WebRequest req) {
		return new ResponseEntity<String>("Author Not Found", HttpStatus.NOT_FOUND);
	}
}
