package com.tom.template.dto;

import com.tom.template.util.validation.EmailNotInUse;
import com.tom.template.util.validation.ValidEmail;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

@Getter
@ApiModel(description = "Change an email address")
public class ChangeEmail {
	
	@ValidEmail
	@EmailNotInUse
	private String email;
	
}
