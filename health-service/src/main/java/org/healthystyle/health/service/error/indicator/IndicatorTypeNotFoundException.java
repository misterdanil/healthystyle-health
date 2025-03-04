package org.healthystyle.health.service.error.indicator;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class IndicatorTypeNotFoundException extends AbstractException {
	private Long id;

	public IndicatorTypeNotFoundException(Long id, BindingResult result) {
		super(result, "There is no an indicator type by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
