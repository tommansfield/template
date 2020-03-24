package com.tom.template.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(description = "Standard API Response")
public class ApiResponse {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timeStamp = LocalDateTime.now();
    private int status;
    private String message;
	
    public ApiResponse(HttpStatus status, String message) {
    	this.status = status.value();
    	this.message = message;
    }
    
    public ResponseEntity<ApiResponse> send() {
    	return ResponseEntity.status(status).body(this);
    }
}
