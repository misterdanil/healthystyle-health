package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class DietNotFoundException extends AbstractException {
	private Long id;

	public DietNotFoundException(Long id, BindingResult result) {
		super(result, "There is no a diet with id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
