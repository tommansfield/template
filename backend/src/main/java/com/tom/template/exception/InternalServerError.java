package com.tom.template.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends RuntimeException {

	private static final long serialVersionUID = -3348725990677685855L;
	
	public InternalServerError(String message) {
		super(message);
	}
	
}
