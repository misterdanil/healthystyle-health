package org.healthystyle.health.service.error.sport;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class StepNotFoundException extends AbstractException {
	private Long id;

	public StepNotFoundException(Long id, BindingResult result) {
		super(result, "Could not found step by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
