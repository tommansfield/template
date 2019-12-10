package com.tom.template.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.tom.template.util.validation.Password;
import com.tom.template.util.validation.PasswordMatches;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@PasswordMatches
public class ChangePassword implements Password {
	
	private String oldPassword;
	
	@Size(min = 8, max = 50)
	@NotNull(message = "No new password provided.")
	private String password;
	
	@NotNull(message = "No matching password provided.")
	private String matchingPassword;
	
}
