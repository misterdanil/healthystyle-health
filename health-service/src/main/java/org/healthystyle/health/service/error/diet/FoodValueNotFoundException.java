package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class FoodValueNotFoundException extends AbstractException {
	private Long id;

	public FoodValueNotFoundException(Long id, BindingResult result) {
		super(result, "Could not found food value by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
