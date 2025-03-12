package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class ConvertTypeNotRecognizedException extends AbstractException {
	private String value;

	public ConvertTypeNotRecognizedException(BindingResult result, String value) {
		super(result, "Could not recognize a convert type by value '%s'", value);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
