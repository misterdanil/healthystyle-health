package org.healthystyle.health.service;

import org.healthystyle.health.model.Health;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class RequestHealthAccessor implements HealthAccessor {

	@Override
	public Health getHealth() {
		return (Health) RequestContextHolder.getRequestAttributes().getAttribute("health",
				RequestAttributes.SCOPE_REQUEST);
	}

}
