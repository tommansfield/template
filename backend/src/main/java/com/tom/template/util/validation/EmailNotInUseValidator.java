package com.tom.template.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.tom.template.repository.UserRepository;

public class EmailNotInUseValidator implements ConstraintValidator<EmailNotInUse, String> {

	@Autowired
	private UserRepository userRep;
	
	@Override
	public void initialize(EmailNotInUse constraintAnnotation) {
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == null || StringUtils.isEmpty(email)) {
			return true;
		}
		return !userRep.existsByEmail(email);
	}
}
