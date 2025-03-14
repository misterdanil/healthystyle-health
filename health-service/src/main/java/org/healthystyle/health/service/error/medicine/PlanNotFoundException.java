package org.healthystyle.health.service.error.medicine;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class PlanNotFoundException extends AbstractException {
	private Long id;

	public PlanNotFoundException(Long id, BindingResult result) {
		super(result, "Could not found plan by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
