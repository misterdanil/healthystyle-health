package org.healthystyle.health.service.error.measure.convert;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class ConvertTypeNotFoundException extends AbstractException {
	private Long id;

	public ConvertTypeNotFoundException(Long id, BindingResult result) {
		super(result, "There is no convert type by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
