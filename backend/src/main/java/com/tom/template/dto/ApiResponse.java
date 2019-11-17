package com.tom.template.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.tom.template.util.MessageUtils;

@Component
public class ApiResponse {
	
	@Autowired
	private MessageUtils messages;
	
	public ResponseEntity<?> send(HttpStatus status, String message, Object... args) {
		return ResponseEntity.status(status).body(messages.getMessage(message, args));
	}
}
