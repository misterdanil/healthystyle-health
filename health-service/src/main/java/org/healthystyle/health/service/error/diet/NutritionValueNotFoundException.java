package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.model.diet.Value;
import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class NutritionValueNotFoundException extends AbstractException {
	private Value value;

	public NutritionValueNotFoundException(Value value, BindingResult result) {
		super(result, "Could not found nutrition value by value '%s'", value);
		this.value = value;
	}

	public Value getValue() {
		return value;
	}

}
