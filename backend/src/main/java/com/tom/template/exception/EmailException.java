package com.tom.template.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class EmailException extends RuntimeException {

	private static final long serialVersionUID = 8321015887398482491L;
	
	public EmailException(String message) {
		super(message);
	}
	
}
