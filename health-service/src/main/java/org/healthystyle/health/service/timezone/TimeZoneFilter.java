package org.healthystyle.health.service.timezone;

import java.io.IOException;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.Timezone;
import org.healthystyle.health.repository.timezone.TimeZoneContext;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.error.UserUnathenticatedException;
import org.healthystyle.health.service.error.timezone.TimeZoneExistException;
import org.healthystyle.health.service.error.timezone.UnknownTimeZoneException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TimeZoneFilter extends OncePerRequestFilter {
	@Autowired
	private TimeZoneService service;
	@Autowired
	private HealthAccessor healthAccessor;

	private static final Logger LOG = LoggerFactory.getLogger(TimeZoneFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			Health health = healthAccessor.getHealth();
			Timezone timezone = null;
			if (health != null) {
				timezone = service.findByUserId(health.getUserId());
			}
			String headerTimezone = request.getHeader("TimeZone");
			if (headerTimezone != null && !headerTimezone.isBlank()) {
				try {
					if (timezone == null) {
						service.save(headerTimezone);
					} else if (!timezone.getTimezone().equals(headerTimezone)) {
						service.update(headerTimezone);
					}
				} catch (UserUnathenticatedException | UnknownTimeZoneException | TimeZoneExistException e) {
					LOG.debug("Could not set timezone: " + headerTimezone, e);
				}
				TimeZoneContext.setTimezone(headerTimezone);
			} else if (timezone != null) {
				TimeZoneContext.setTimezone(timezone.getTimezone());
			}
			filterChain.doFilter(request, response);
		} finally {
			TimeZoneContext.clear();
		}
	}

}
