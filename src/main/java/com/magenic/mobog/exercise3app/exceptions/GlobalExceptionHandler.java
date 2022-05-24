package com.magenic.mobog.exercise3app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

	// TODO add a message class for a more descriptive error message
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<String> handleBadRequestException(InvalidRequestException ex, WebRequest req){
		return ResponseEntity.status(ex.getHttpStatus()).body("request format invalid");
	}
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(EntityNotFoundException ex, WebRequest req){
		return ResponseEntity.status(ex.getHttpStatus()).body("resource not found");
	}
}
