package org.healthystyle.health.service.error.indicator;

import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class NotConvertableException extends AbstractException {
	private String value;
	private ConvertType convertType;

	public NotConvertableException(String value, ConvertType convertType, BindingResult result) {
		super(result, "This value '%s' is not convertable to this type '%s'", value, convertType.getClass().getName());
		this.value = value;
		this.convertType = convertType;
	}

	public String getValue() {
		return value;
	}

	public ConvertType getConvertType() {
		return convertType;
	}

}
