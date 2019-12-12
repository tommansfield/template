package com.tom.template.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.tom.template.util.validation.Password;
import com.tom.template.util.validation.PasswordMatches;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@PasswordMatches
@ApiModel(description = "Change a password")
public class ChangePassword implements Password {
	
	@ApiModelProperty
	private String oldPassword;
	
	@Size(min = 8, max = 50)
	@NotNull(message = "No new password provided.")
	@ApiModelProperty
	private String password;
	
	@NotNull(message = "No matching password provided.")
	private String matchingPassword;
	
}
