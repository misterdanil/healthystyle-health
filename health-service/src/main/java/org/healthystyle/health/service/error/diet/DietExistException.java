package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class DietExistException extends AbstractException {
	private String title;

	public DietExistException(String title, BindingResult result) {
		super(result, "A diet with title '%s' has already existed", title);
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
