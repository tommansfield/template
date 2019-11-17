package com.tom.template.dto;

import com.tom.template.util.validation.EmailNotInUse;
import com.tom.template.util.validation.ValidEmail;
import lombok.Getter;

@Getter
public class ChangeEmail {
	
	@ValidEmail
	@EmailNotInUse
	private String email;
	
}
