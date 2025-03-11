package org.healthystyle.health.service.validation.annotation.validator;

import java.util.Arrays;
import java.util.Collection;

import org.healthystyle.health.service.validation.annotation.XORNotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class XORNotNullValidator implements ConstraintValidator<XORNotNull, Object> {
	private String[] fields;
	private boolean isEmpty;

	private static final Logger LOG = LoggerFactory.getLogger(XORNotNullValidator.class);

	@Override
	public void initialize(XORNotNull constraintAnnotation) {
		LOG.debug("Getting fields names");
		fields = constraintAnnotation.fields();
		isEmpty = constraintAnnotation.empty();
		LOG.debug("Got field names '{}'", Arrays.asList(fields));
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		LOG.debug("Validating value for XOR fields");
		BeanWrapper wrapper = new BeanWrapperImpl(value);
		int counter = 0;
		LOG.debug("Counting not null fields");
		for (String field : fields) {
			Object propertyValue = wrapper.getPropertyValue(field);
			if (propertyValue != null) {
				if (!isEmpty && (propertyValue instanceof Collection && ((Collection<?>) propertyValue).isEmpty()
						|| propertyValue instanceof CharSequence && ((CharSequence) propertyValue).isEmpty())) {
					continue;
				}
				LOG.debug("Field '{}' with value '{}' is not null", field, propertyValue);
				counter++;
				if (counter > 1) {
					LOG.warn("Count is more than 1: {}", counter);
					return false;
				}
			}
		}

		if (counter == 1) {
			LOG.debug("There is only one not null field value");
			return true;
		} else {
			LOG.debug("Count {} is not valid. It must be 1");
			return false;
		}
	}

}
