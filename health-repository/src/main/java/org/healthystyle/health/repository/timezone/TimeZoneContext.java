package org.healthystyle.health.repository.timezone;

import java.time.ZoneId;
import java.time.zone.ZoneRulesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeZoneContext {
	private static final ThreadLocal<String> TIMEZONE = new ThreadLocal<>();

	private static final Logger LOG = LoggerFactory.getLogger(TimeZoneContext.class);

	public static void setTimezone(String timezone) {
		try {
			ZoneId.of(timezone);
			TIMEZONE.set(timezone);
		} catch (ZoneRulesException e) {
			LOG.warn("Could not define timezone to set thread local: " + timezone, e);
		}
	}

	public static String getTimezone() {
		return TIMEZONE.get();
	}

	public static void clear() {
		TIMEZONE.remove();
	}
}
