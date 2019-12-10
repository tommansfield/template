package com.tom.template.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {

	private static final long serialVersionUID = 8321015887398482491L;
	
	public InternalServerException(String message) {
		super(message);
	}
	
	public InternalServerException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
