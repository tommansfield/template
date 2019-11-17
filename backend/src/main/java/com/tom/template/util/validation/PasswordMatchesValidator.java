package com.tom.template.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object>{

	@Override
	public void initialize(PasswordMatches constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		Password signUp = (Password) object;
		return signUp.getPassword().equals(signUp.getMatchingPassword());
	}
	
}
