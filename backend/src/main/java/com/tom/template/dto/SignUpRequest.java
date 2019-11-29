package com.tom.template.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
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

	@Column(length = 36)
	private String fullName;
	
	@NotNull(message = "No password provided.")
	private String password;
	
	@NotNull(message = "No matching password provided.")
	private String matchingPassword;
	
}
