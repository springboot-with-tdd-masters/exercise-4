package com.csv.bookscrudexercise4.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNameNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public UserNameNotFoundException() {
		super("Username Not Found");
	}
	
	public UserNameNotFoundException(String message) {
		super(message);
	}

	public UserNameNotFoundException(String message, Throwable t) {
		super(message, t);
	}
}
