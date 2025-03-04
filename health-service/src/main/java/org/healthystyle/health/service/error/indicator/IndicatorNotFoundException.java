package org.healthystyle.health.service.error.indicator;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class IndicatorNotFoundException extends AbstractException {
	private Long id;

	public IndicatorNotFoundException(Long id, BindingResult result) {
		super(result, "There is no an indicator with id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
