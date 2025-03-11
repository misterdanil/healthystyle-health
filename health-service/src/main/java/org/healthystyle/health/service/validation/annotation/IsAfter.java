package org.healthystyle.health.service.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.healthystyle.health.service.validation.annotation.validator.IsAfterValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsAfterValidator.class)
public @interface IsAfter {
	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String beforeField();

	String afterField();
}
