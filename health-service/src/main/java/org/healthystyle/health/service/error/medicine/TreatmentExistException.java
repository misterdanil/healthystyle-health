package org.healthystyle.health.service.error.medicine;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class TreatmentExistException extends AbstractException {
	private String description;

	public TreatmentExistException(String description, BindingResult result) {
		super(result, "A treatment has already existed by description '%s'", description);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
