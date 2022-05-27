package com.magenic.mobog.exercise4app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<String> handleInternalServerException(InternalServerException ex, WebRequest req){
		return ResponseEntity.status(ex.getHttpStatus()).body("unable to process request");
	}
	@ExceptionHandler(UserExistsException.class)
	public ResponseEntity<String> handleUserExistsException(UserExistsException ex, WebRequest req){
		return ResponseEntity.status(ex.getHttpStatus()).body("username already exists");
	}
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<String> handleUsernameNotFound(UsernameNotFoundException ex, WebRequest req){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("user not allowed.");
	}@ExceptionHandler(UserNotAllowedException.class)
	public ResponseEntity<String> handleUsernameNotFound(UserNotAllowedException ex, WebRequest req){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}
}
