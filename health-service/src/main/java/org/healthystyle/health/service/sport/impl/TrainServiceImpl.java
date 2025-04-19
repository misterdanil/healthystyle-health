package org.healthystyle.health.service.sport.impl;

import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.health.model.sport.Sport;
import org.healthystyle.health.model.sport.Train;
import org.healthystyle.health.repository.sport.TrainRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.dto.sport.SetSaveRequest;
import org.healthystyle.health.service.dto.sport.TrainSaveRequest;
import org.healthystyle.health.service.dto.sport.TrainUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.error.sport.SportNotFoundException;
import org.healthystyle.health.service.error.sport.TrainExistException;
import org.healthystyle.health.service.error.sport.TrainNotFoundException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.sport.SetService;
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
public class TrainServiceImpl implements TrainService {
	@Autowired
	private TrainRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private SetService setService;
	@Autowired
	private SportService sportService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(TrainServiceImpl.class);

	@Override
	public Train findById(Long id) throws ValidationException, TrainNotFoundException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "train");
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("train.find.id.not_null", "Укажите идентификатор для поиска");
			throw new ValidationException("The id is null", result);
		}

		LOG.debug("Checking train for existence by id '{}'", id);
		Optional<Train> train = repository.findById(id);
		if (train.isEmpty()) {
			result.reject("train.find.not_found", "Не удалось найти тренировку");
			throw new TrainNotFoundException(id, result);
		}
		LOG.info("Got train successfully by id '{}'", id);

		return train.get();
	}

	@Override
	public Page<Train> findByDescription(String description, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_DESCRIPTION_PARAM_NAMES, description, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "set");
		if (description == null || description.isBlank()) {
			result.reject("train.find.description.not_blank", "Укажите описание тренировки для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Page<Train> trains = repository.findByDescription(description, healthId, PageRequest.of(page, limit));
		LOG.info("Got trains successfully by params: {}", params);

		return trains;
	}

	@Override
	public Page<Train> findByDay(Integer day, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_DAY_PARAM_NAMES, day, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "set");
		if (day == null) {
			result.reject("train.find.day.not_blank", "Укажите день тренировки для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Page<Train> trains = repository.findByDay(day, healthId, PageRequest.of(page, limit));
		LOG.info("Got trains successfully by params: {}", params);

		return trains;
	}

	@Override
	public Page<Train> findByDate(Instant date, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_DATE_PARAM_NAMES, date, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "set");
		if (date == null) {
			result.reject("train.find.date.not_null", "Укажите дату тренировки для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Page<Train> trains = repository.findByDate(date, healthId, PageRequest.of(page, limit));
		LOG.info("Got trains successfully by params: {}", params);

		return trains;
	}

	@Override
	public Page<Train> findBySport(Long sportId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_SPORT_PARAM_NAMES, sportId, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "set");
		if (sportId == null) {
			result.reject("train.find.sport_id.not_null", "Укажите занятие для поиска тренировок");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Page<Train> trains = repository.findBySport(sportId, PageRequest.of(page, limit));
		LOG.info("Got trains successfully by params: {}", params);

		return trains;
	}

	@Override
	public Set<Train> findPlanned(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PLANNED_PARAM_NAMES, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "set");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Set<Train> trains = repository.findPlanned(healthId, page, limit);
		LOG.info("Got trains successfully by params: {}", params);

		return trains;
	}

	@Override
	public List<Train> findNextTrain() {
		Long healthId = healthAccessor.getHealth().getId();

		List<Train> trains = repository.findNextTrain(healthId);
		LOG.info("Got trains successfully");
		return trains;
	}

	@Override
	public Train save(TrainSaveRequest saveRequest, Long sportId)
			throws ValidationException, TrainExistException, TrainNotFoundException, ExerciseNotFoundException, SportNotFoundException {
		LOG.debug("Validating train: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "train");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The train is invalid: %s. Result: %s", result, saveRequest, result);
		}

		Integer day = saveRequest.getDay();
		LocalTime time = saveRequest.getTime().truncatedTo(ChronoUnit.SECONDS);
		LOG.debug("Checking train for existence by day '{}' and time '{}'", day, time);
		if (repository.existsByDayAndTime(day, time)) {
			result.reject("train.save.exists", "Тренировка с таким днём и временем уже существует");
			throw new TrainExistException(day, time, result);
		}

		LOG.debug("The train is OK: {}", saveRequest);

		Sport sport = sportService.findById(sportId);

		Train train = new Train(saveRequest.getDescription(), time, day, sport);
		train = repository.save(train);
		LOG.info("Train was saved successfully: {}", train);

		List<SetSaveRequest> setSaveRequests = saveRequest.getSets();
		LOG.debug("Saving sets: {}", setSaveRequests);
		for (SetSaveRequest setSaveRequest : setSaveRequests) {
			LOG.debug("Saving set: {}", setSaveRequest);
			setService.save(setSaveRequest, train.getId());
		}

		return train;
	}

	@Override
	public void update(TrainUpdateRequest updateRequest, Long id)
			throws ValidationException, TrainExistException, TrainNotFoundException {
		LOG.debug("Validating train: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "train");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The train is invalid: %s. Result: %s", result, updateRequest, result);
		}

		Train train = findById(id);

		Integer day = updateRequest.getDay();
		LocalTime time = updateRequest.getTime().truncatedTo(ChronoUnit.SECONDS);
		if (day != train.getDay() || !time.equals(train.getTime())) {
			LOG.debug("Checking train for existence by day '{}' and time '{}'", day, time);
			if (repository.existsByDayAndTime(day, time)) {
				result.reject("train.save.exists", "Тренировка с таким днём и временем уже существует");
				throw new TrainExistException(day, time, result);
			}

			train.setDay(day);
			train.setTime(time);
		}

		LOG.debug("The train is OK: {}", updateRequest);

		String description = updateRequest.getDescription();
		if (!description.equals(train.getDescription())) {
			train.setDescription(description);
		}

		train = repository.save(train);
		LOG.info("Train was updated successfully: {}", train);
	}

}
