package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class FoodSetExistException extends AbstractException {
	private String title;

	public FoodSetExistException(String title, BindingResult result) {
		super(result, "Food set has already existed with title '%s'", title);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
