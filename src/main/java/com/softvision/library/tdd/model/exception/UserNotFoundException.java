package com.softvision.library.tdd.model.exception;


public class UserNotFoundException extends UnauthorizedException {
    public UserNotFoundException() {
        super("User not found.");
    }
}
