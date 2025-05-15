package org.healthystyle.health.service;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.health.HealthNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class HealthAccessorImpl implements HealthAccessor {
	@Autowired
	private HealthService service;

	private static final Logger LOG = LoggerFactory.getLogger(HealthAccessorImpl.class);

	@Override
	public Health getHealth() {
		Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
		try {
			return service.findByUserId(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
		} catch (ValidationException e) {
			LOG.error("Something wrong happened while getting health. The user id is invalid: {}", e, id);
		} catch (HealthNotFoundException e) {
			LOG.error("Could not get health by authenticated user's id '{}'. The health hasn't been initialized: {}", e,
					id);
		}

		return null;
	}

}
