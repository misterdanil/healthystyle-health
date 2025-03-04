package org.healthystyle.health.service.error.health;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class AlreadyInitializedException extends AbstractException {
	private Health health;

	public AlreadyInitializedException(Health health, BindingResult result) {
		super(result, "A health with id '%s' has already existed for user '%s'",
				new Object[] { health.getId(), health.getUserId() });
		this.health = health;
	}

	public Health getHealth() {
		return health;
	}

}
