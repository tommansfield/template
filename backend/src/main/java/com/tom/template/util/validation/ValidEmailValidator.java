package com.tom.template.util.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

	private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
    						
    
	@Override
	public void initialize(ValidEmail constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == null || StringUtils.isEmpty(email)) {
			return false;
		}
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public static boolean isValid(String email) {
		ValidEmailValidator validator = new ValidEmailValidator();
		return validator.isValid(email, null);
	}

}
