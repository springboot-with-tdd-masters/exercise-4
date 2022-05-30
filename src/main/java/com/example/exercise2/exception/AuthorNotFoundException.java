package com.example.exercise2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AuthorNotFoundException extends RuntimeException  {
  private static final long serialVersionUID = -2433483504677527984L;

  public AuthorNotFoundException() {
    super("Author Not Found");
  }

  public AuthorNotFoundException(String message) {
    super(message);
  }
}
