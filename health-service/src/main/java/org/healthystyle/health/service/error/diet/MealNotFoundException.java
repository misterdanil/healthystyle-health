package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class MealNotFoundException extends AbstractException {
	private Long id;

	public MealNotFoundException(Long id, BindingResult result) {
		super(result, "There is no a meal with id '%s'", result);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
