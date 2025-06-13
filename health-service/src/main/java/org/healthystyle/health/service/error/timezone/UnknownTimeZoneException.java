package org.healthystyle.health.service.error.timezone;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class UnknownTimeZoneException extends AbstractException {
	private String timezone;

	public UnknownTimeZoneException(BindingResult result, Throwable cause, String timezone) {
		super(result, "Could not define time zone: %S", cause, timezone);
		this.timezone = timezone;
	}

	public String getTimezone() {
		return timezone;
	}

}
