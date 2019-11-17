package com.tom.template.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenResponse {
	
	private String accessToken;
    private String tokenType = "Bearer";

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
    
}
