package org.healthystyle.health.service.validation.annotation.validator;

import java.time.LocalDate;

import org.healthystyle.health.service.validation.annotation.IsAfter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsAfterValidator implements ConstraintValidator<IsAfter, Object> {
	private String beforeField;
	private String afterField;

	private static final Logger LOG = LoggerFactory.getLogger(IsAfterValidator.class);

	@Override
	public void initialize(IsAfter constraintAnnotation) {
		LOG.debug("Getting fields names");
		beforeField = constraintAnnotation.beforeField();
		afterField = constraintAnnotation.afterField();
		LOG.debug("Got fields names: before '{}' and after '{}'", beforeField, afterField);
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		LOG.debug("Validating the date: {}", value);
		if (beforeField == null || afterField == null) {
			LOG.warn("The fields names must be not null: {}, {}", beforeField, afterField);
			return false;
		}

		LOG.debug("Getting date values");
		BeanWrapper wrapper = new BeanWrapperImpl(value);
		LocalDate beforeDate = (LocalDate) wrapper.getPropertyValue(beforeField);
		LocalDate afterDate = (LocalDate) wrapper.getPropertyValue(afterField);
		if (beforeDate == null || afterDate == null) {
			LOG.warn("Before field and after field must be not null: {}, {}", beforeDate, afterDate);
			return false;
		}

		return afterDate.isAfter(beforeDate);
	}

}
