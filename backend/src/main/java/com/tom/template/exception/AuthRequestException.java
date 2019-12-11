package com.tom.template.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthRequestException extends RuntimeException {

	private static final long serialVersionUID = 6474335161985001979L;

	public AuthRequestException() {
		super();
	}
	
	public AuthRequestException(String message) {
        super(message);
    }

}
