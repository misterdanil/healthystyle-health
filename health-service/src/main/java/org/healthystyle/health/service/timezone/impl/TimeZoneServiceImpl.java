package org.healthystyle.health.service.timezone.impl;

import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.HashMap;
import java.util.TimeZone;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.Timezone;
import org.healthystyle.health.repository.timezone.TimeZoneRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.error.UserUnathenticatedException;
import org.healthystyle.health.service.error.timezone.TimeZoneExistException;
import org.healthystyle.health.service.error.timezone.UnknownTimeZoneException;
import org.healthystyle.health.service.timezone.TimeZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@Service
public class TimeZoneServiceImpl implements TimeZoneService {
	@Autowired
	private TimeZoneRepository repository;
	@Autowired
	private HealthAccessor healthAccessor;

	@Override
	@Cacheable("timezones")
	public Timezone findByUserId(Long userId) {
		if (userId == null) {
			return null;
		}

		Timezone timezone = repository.findByUserId(userId);

		return timezone;
	}

	@Override
	public Timezone save(String timeZone)
			throws UnknownTimeZoneException, UserUnathenticatedException, TimeZoneExistException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "timezone");

		Health health = healthAccessor.getHealth();

		checkAuth(health, result);

		checkTimeZone(timeZone, result);

		if (repository.existsByUserId(health.getUserId())) {
			result.reject("timezone.save.exists", "У пользователя уже есть часовой пояс");
			throw new TimeZoneExistException(result, health.getUserId());
		}

		Long userId = health.getUserId();
		Timezone timezone = new Timezone(userId, timeZone);

		timezone = repository.save(timezone);

		return timezone;
	}

	private void checkAuth(Health health, BindingResult result) throws UserUnathenticatedException {
		if (health == null) {
			result.reject("timezone.save.unauthenticated", "Пользователь неаутентифицирован");
			throw new UserUnathenticatedException(result);
		}
	}

	private void checkTimeZone(String timeZone, BindingResult result) throws UnknownTimeZoneException {
		try {
			ZoneId.of(timeZone);
		} catch (ZoneRulesException e) {
			result.reject("timezone.save.timezone.unknown", "Не удалось опеределить часовой пояс");
			throw new UnknownTimeZoneException(result, e, timeZone);
		}
	}

	@Override
	public void update(String timezone) throws UnknownTimeZoneException, UserUnathenticatedException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "timezone");

		Health health = healthAccessor.getHealth();

		checkAuth(health, result);

		checkTimeZone(timezone, result);

		Timezone currentTimezone = findByUserId(health.getUserId());
		currentTimezone.setTimezone(timezone);
		repository.save(currentTimezone);
	}

	public static void main(String[] args) {
		System.out.println(TimeZone.getTimeZone(ZoneId.of("Europe/Mdddoscow")));
	}

}
