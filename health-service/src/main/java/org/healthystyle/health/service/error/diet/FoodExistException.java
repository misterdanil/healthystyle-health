package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class FoodExistException extends AbstractException {
	private String title;

	public FoodExistException(String title, BindingResult result) {
		super(result, "Could not found a food by title '%s'", title);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
