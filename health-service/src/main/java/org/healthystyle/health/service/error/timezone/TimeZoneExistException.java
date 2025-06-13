package org.healthystyle.health.service.error.timezone;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class TimeZoneExistException extends AbstractException {
	private Long userId;

	public TimeZoneExistException(BindingResult result, Long userId) {
		super(result, "Time zone with user id '%s' has already existed", userId);
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

}
