package com.brownbag.exercise4.errorhandler;

public class AuthorNotFoundException extends Exception {

    public AuthorNotFoundException() {
        super("Author Not Found Exception.");
    }
}
