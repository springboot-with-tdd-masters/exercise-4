package com.example.exercisefour.exception;
public class ErrorMsgWrapper {
    private String error;

    public ErrorMsgWrapper(String msg) {
        this.error = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}