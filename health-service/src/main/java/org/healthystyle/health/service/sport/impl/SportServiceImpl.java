package org.healthystyle.health.service.sport.impl;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.sport.Sport;
import org.healthystyle.health.model.sport.Train;
import org.healthystyle.health.repository.sport.SportRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.dto.sport.SportSaveRequest;
import org.healthystyle.health.service.dto.sport.SportUpdateRequest;
import org.healthystyle.health.service.dto.sport.TrainSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.error.sport.SportNotFoundException;
import org.healthystyle.health.service.error.sport.TrainExistException;
import org.healthystyle.health.service.error.sport.TrainNotFoundException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.sport.SportService;
import org.healthystyle.health.service.sport.TrainService;
import org.healthystyle.health.service.validation.ParamsChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

@Service
public class SportServiceImpl implements SportService {
	@Autowired
	private SportRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private TrainService trainService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(SportServiceImpl.class);

	@Override
	public Sport findById(Long id) throws ValidationException, SportNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "sport");

		LOG.debug("Checking id for not null");
		if (id == null) {
			result.reject("sport.find.id.not_null", "Укажите идентификатор занятия для поиска");
			throw new ValidationException("The id is null", result);
		}

		Optional<Sport> sport = repository.findById(id);
		LOG.debug("Checking sport for existence by id '{}'", id);
		if (sport.isEmpty()) {
			result.reject("sport.find.not_found", "Не удалось найти занятие");
			throw new SportNotFoundException(id, result);
		}
		LOG.info("Got sport by id '{}' successfully", id);

		return sport.get();
	}

	@Override
	public Page<Sport> findByDescription(String description, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_DESCRIPTION_PARAM_NAMES, description, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "sport");
		LOG.debug("Validating params: {}", params);
		if (description == null || description.isBlank()) {
			result.reject("sport.find.description.not_empty", "Укажите описание для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Long healthId = healthAccessor.getHealth().getId();

		Page<Sport> sports = repository.findByDescription(description, healthId, PageRequest.of(page, limit));
		LOG.info("Got sports successfully by params: {}", params);

		return sports;
	}

	@Override
	public Page<Sport> findActual(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_ACTUAL_PARAM_NAMES, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "sport");
		LOG.debug("Validating params: {}", params);
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Long healthId = healthAccessor.getHealth().getId();

		Page<Sport> sports = repository.findActual(healthId, PageRequest.of(page, limit));
		LOG.info("Got sports successfully by params: {}", params);

		return sports;
	}

	@Override
	public Sport save(SportSaveRequest saveRequest)
			throws ValidationException, TrainExistException, TrainNotFoundException, ExerciseNotFoundException, SportNotFoundException {
		LOG.debug("Validating sport: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "sport");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("Sport is invalid: %s. Result: %s", result, saveRequest, result);
		}

		Health health = healthAccessor.getHealth();

		LOG.debug("Sport is OK: {}", saveRequest);
		Sport sport = new Sport(saveRequest.getDescription(), saveRequest.getStart(), saveRequest.getEnd(), health);
		sport = repository.save(sport);
		LOG.info("Sport was saved successfully: {}", sport);

		List<TrainSaveRequest> trainSaveRequests = saveRequest.getTrains();
		LOG.debug("Saving trains: {}", trainSaveRequests);
		for (TrainSaveRequest trainSaveRequest : trainSaveRequests) {
			LOG.debug("Saving train: {}", trainSaveRequest);
			Train train = trainService.save(trainSaveRequest, sport.getId());
			sport.addTrain(train);
		}

		return sport;
	}

	@Override
	public void update(SportUpdateRequest updateRequest, Long id) throws ValidationException, SportNotFoundException {
		LOG.debug("Validating sport: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "sport");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("Sport is invalid: %s. Result: %s", result, updateRequest, result);
		}

		Sport sport = findById(id);

		String description = updateRequest.getDescription();
		if (!description.equals(sport.getDescription())) {
			LOG.debug("Setting description: {}", description);
			sport.setDescription(description);
		}

		LocalDate start = updateRequest.getStart();
		if (!start.equals(sport.getStart())) {
			LOG.debug("Setting start: {}", start);
			sport.setStart(start);
		}

		LocalDate end = updateRequest.getEnd();
		if (!end.equals(sport.getEnd())) {
			LOG.debug("Setting end: {}", end);
			sport.setEnd(end);
		}

		sport = repository.save(sport);
		LOG.info("Sport was updated successfully: {}", sport);
	}

}
