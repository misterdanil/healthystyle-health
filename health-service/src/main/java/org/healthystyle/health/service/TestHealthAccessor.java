package org.healthystyle.health.service;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.repository.HealthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestHealthAccessor implements HealthAccessor {
	@Autowired
	private HealthRepository healthRepository;

	@Override
	public Health getHealth() {
		return healthRepository.findByUserId(1L);
	}

}
