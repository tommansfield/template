package com.tom.template.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class ErrorResponse {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
    
    public ErrorResponse(int status, String message, String path) {
    	this.timestamp = LocalDateTime.now();
    	this.status = status;
    	this.message = message;
    	this.path = path;
    }
	    
}
