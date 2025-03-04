package org.healthystyle.health.service;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.health.AlreadyInitializedException;
import org.healthystyle.health.service.error.health.HealthNotFoundException;

public interface HealthService {
	Health init(Long userId) throws ValidationException, AlreadyInitializedException;

	Health findByUserId(Long userId) throws ValidationException, HealthNotFoundException;
}
