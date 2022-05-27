package com.example.exercise2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException  {
  private static final long serialVersionUID = -2433483504677527984L;

  public BookNotFoundException() {
    super("Book Not Found");
  }

  public BookNotFoundException(String message) {
    super(message);
  }
}
