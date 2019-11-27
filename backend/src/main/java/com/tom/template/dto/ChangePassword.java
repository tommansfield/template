package com.tom.template.dto;

import javax.validation.constraints.NotNull;
import com.tom.template.util.validation.Password;
import com.tom.template.util.validation.PasswordMatches;
import com.tom.template.util.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@PasswordMatches
public class ChangePassword implements Password {
	
	private String oldPassword;
	
	@ValidPassword
	@NotNull(message = "No new password provided.")
	private String password;
	
	@NotNull(message = "No matching password provided.")
	private String matchingPassword;
	
}
