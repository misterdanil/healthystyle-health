package org.healthystyle.health.service.error.medicine;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class TreatmentNotFoundException extends AbstractException {
	private Long id;

	public TreatmentNotFoundException(Long id, BindingResult result) {
		super(result, "Could not found treatment by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
