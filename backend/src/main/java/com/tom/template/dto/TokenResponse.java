package com.tom.template.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(description = "Access token response")
public class TokenResponse {
	
	private String accessToken;
    private String tokenType = "Bearer";

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
    
}
