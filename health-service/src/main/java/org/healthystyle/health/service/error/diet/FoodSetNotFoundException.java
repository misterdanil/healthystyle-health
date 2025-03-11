package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class FoodSetNotFoundException extends AbstractException {
	private Long id;

	public FoodSetNotFoundException(Long id, BindingResult result) {
		super(result, "Could not found a food set by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
