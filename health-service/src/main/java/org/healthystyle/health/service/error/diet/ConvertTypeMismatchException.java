package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class ConvertTypeMismatchException extends AbstractException {
	private Long convertTypeId;
	private String value;

	public ConvertTypeMismatchException(Long convertTypeId, String value, BindingResult result) {
		super(result, "The value '%s' doesn't match convert type '%s'", value, convertTypeId);
		this.convertTypeId = convertTypeId;
		this.value = value;
	}

	public Long getConvertTypeId() {
		return convertTypeId;
	}

	public String getValue() {
		return value;
	}

}
