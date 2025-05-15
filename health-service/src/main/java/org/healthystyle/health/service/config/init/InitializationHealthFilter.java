package org.healthystyle.health.service.config.init;

import java.io.IOException;

import org.healthystyle.health.service.HealthService;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.health.AlreadyInitializedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InitializationHealthFilter extends OncePerRequestFilter {
	@Autowired
	private HealthService healthService;

	private static final Logger LOG = LoggerFactory.getLogger(InitializationHealthFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		LOG.debug("Check user is authenticated");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			LOG.debug("User is not authenticated");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			healthService.init(Long.valueOf(authentication.getName()));
		} catch (ValidationException e) {
			LOG.error("Something wrong happened while initializaing user: {}", authentication.getName(), e);
			filterChain.doFilter(request, response);
		} catch (AlreadyInitializedException e) {
			filterChain.doFilter(request, response);
		}
	}

}
