package com.brownbag.exercise4.errorhandler;

public class BookNotFoundException extends Exception {

    public BookNotFoundException() {
        super("Book Not Found Exception.");
    }
}
