package com.tom.template.util.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailNotInUseValidator.class)
@Documented
public @interface EmailNotInUse {
	String message() default "The email address provided is already registered.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
