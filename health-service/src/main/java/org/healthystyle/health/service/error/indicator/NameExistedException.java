package org.healthystyle.health.service.error.indicator;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class NameExistedException extends AbstractException {
	private String name;

	public NameExistedException(String name, BindingResult result) {
		super(result, "A name for indicator type '%s' has already existed", name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
