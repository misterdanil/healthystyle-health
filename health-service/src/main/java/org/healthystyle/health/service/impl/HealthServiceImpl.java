package org.healthystyle.health.service.impl;

import java.util.LinkedHashMap;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.repository.HealthRepository;
import org.healthystyle.health.service.HealthService;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.health.AlreadyInitializedException;
import org.healthystyle.health.service.error.health.HealthNotFoundException;
import org.healthystyle.health.service.error.health.UserIdField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@Service
public class HealthServiceImpl implements HealthService {
	@Autowired
	private HealthRepository repository;

	private static final Logger LOG = LoggerFactory.getLogger(HealthServiceImpl.class);

	@Override
	public Health init(Long userId) throws ValidationException, AlreadyInitializedException {
		LOG.debug("Start initializing a user with id '{}'", userId);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "health");
		LOG.debug("Start validating a health for user id '{}'", userId);
		if (userId == null) {
			result.reject("health.init.user_id.notNull", "Необходимо передать идентификатор пользователя");
			throw new ValidationException("Exception occurred while initializing a health. User id is null", result);
		}

		LOG.debug("Start validating a health existence for user id '{}'", userId);
		Health health = repository.findByUserId(userId);
		if (health != null) {
			result.reject("health.init.user_id.existed", "Инициализация пользователя уже произведена");
			throw new AlreadyInitializedException(health, result);
		}

		LOG.debug("Saving a health for user '{}'", userId);
		health = new Health(userId);
		health = repository.save(health);
		LOG.info("A health '{}' for user '{}' was successfully saved", health.getId(), userId);

		return health;

	}

	@Override
	public Health findByUserId(Long userId) throws ValidationException, HealthNotFoundException {
		LOG.debug("Start fetching a health by user id '{}'", userId);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "health");
		LOG.debug("Start validating a health for user id '{}'", userId);
		if (userId == null) {
			result.reject("health.find.notNull",
					"Для получения данных о здоровье необходимо указать идентификатор пользователя");
			throw new ValidationException("Exception occurred while fetching a user. User id is null", result);
		}

		LOG.debug("Fetching a health by user id '{}'...", userId);
		Health health = repository.findByUserId(userId);
		if (health == null) {
			result.reject("health.find.notExist",
					"Не существует данных о здоровья для пользователя с таким идентификатором");
			throw new HealthNotFoundException(new UserIdField(userId), result);
		}

		LOG.info("A health by user id '{}' was successfully fetched", userId);

		return health;
	}

}
