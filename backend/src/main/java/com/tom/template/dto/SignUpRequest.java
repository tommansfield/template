package com.tom.template.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.tom.template.util.validation.EmailNotInUse;
import com.tom.template.util.validation.Password;
import com.tom.template.util.validation.PasswordMatches;
import com.tom.template.util.validation.ValidEmail;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@PasswordMatches
public class SignUpRequest implements Password {
	
	@ValidEmail
	@EmailNotInUse
	private String email;
	
	@Size(min = 3, max = 15)
	private String username;

	@NotNull(message = "No password provided.")
	private String password;
	
	@NotNull(message = "No matching password provided.")
	private String matchingPassword;
	
}
