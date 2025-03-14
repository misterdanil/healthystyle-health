package org.healthystyle.health.service.error.medicine;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class WeightNegativeOrZeroException extends AbstractException {
	private String weight;

	public WeightNegativeOrZeroException(String weight, BindingResult result) {
		super(result, "Weight couldn't be negative or zero but passed '%s'", weight);
		this.weight = weight;
	}

	public String getWeight() {
		return weight;
	}

}
