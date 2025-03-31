package org.healthystyle.health.service.error.measure;

import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class MeasureNotFoundException extends AbstractException {
	private Type type;

	public MeasureNotFoundException(Type type, BindingResult result) {
		super(result, "Measure was not found by type '%s'", type);
		this.type = type;
	}

	public Type getType() {
		return type;
	}

}
