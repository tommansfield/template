package com.tom.template.util.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEmailValidator.class)
@Documented
public @interface ValidEmail {
	String message() default "Email address invalid. Please use a valid email address";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
