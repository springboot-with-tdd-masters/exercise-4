package com.example.exercisefour.exception;
import org.springframework.http.HttpStatus;

public class LibraryAppException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private LibraryAppExceptionCode code;

    public LibraryAppException(LibraryAppExceptionCode code){
        super(code.getMessage());
        this.code = code;
    }

    public HttpStatus getStatus(){
        return code.getStatus();
    }
    public String getMessage(){
        return super.getMessage();
    }
}