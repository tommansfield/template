package com.tom.template.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OAuth2Exception extends RuntimeException {

	private static final long serialVersionUID = -8323273069396804601L;
	
	public OAuth2Exception(String message) {
		super(message);
	}
	
	public OAuth2Exception(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
