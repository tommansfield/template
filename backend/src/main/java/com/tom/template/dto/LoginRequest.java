package com.tom.template.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.tom.template.util.validation.ValidEmail;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
	
	@ValidEmail
	private String email;
	
	@Size(min = 3, max = 15)
	private String username;

	@NotNull(message = "No password provided.")
	private String password;
	
}
