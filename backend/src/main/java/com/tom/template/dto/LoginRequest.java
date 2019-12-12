package com.tom.template.dto;

import javax.validation.constraints.NotNull;
import com.tom.template.util.validation.ValidEmail;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(description = "Login with email/password")
public class LoginRequest {
	
	@ValidEmail
	private String email;

	@NotNull(message = "No password provided.")
	private String password;
	
}
