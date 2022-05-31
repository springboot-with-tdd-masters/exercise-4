package com.example.exercisefour.exception;
import org.springframework.http.HttpStatus;

public enum LibraryAppExceptionCode {
    AUTHOR_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "There is no Author with that ID"),
    BOOK_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "There is no Book with that ID"),
    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "There is no record with given IDs"),
    INVALID_NAME_EXCEPTION(HttpStatus.BAD_REQUEST, "Please insert a valid name"),
	UNABLE_TO_MAP_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Error in Mapping the response");

	private HttpStatus status;
    private String message;


    private LibraryAppExceptionCode(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}