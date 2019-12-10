package com.tom.template.dto;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApiResponse {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date timeStamp = new Date();
    private int status;
    private String message;
	
    public ApiResponse(HttpStatus status, String message, Object... args) {
    	this.status = status.value();
    	this.message = message;
    }
    
    public ResponseEntity<ApiResponse> send() {
    	return ResponseEntity.status(status).body(this);
    }
}
