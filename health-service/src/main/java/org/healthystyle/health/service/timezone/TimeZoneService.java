package org.healthystyle.health.service.timezone;

import org.healthystyle.health.model.Timezone;
import org.healthystyle.health.service.error.UserUnathenticatedException;
import org.healthystyle.health.service.error.timezone.TimeZoneExistException;
import org.healthystyle.health.service.error.timezone.UnknownTimeZoneException;

public interface TimeZoneService {
	Timezone findByUserId(Long userId);

	Timezone save(String timezone) throws UnknownTimeZoneException, UserUnathenticatedException, TimeZoneExistException;

	void update(String timezone) throws UnknownTimeZoneException, UserUnathenticatedException;

}
