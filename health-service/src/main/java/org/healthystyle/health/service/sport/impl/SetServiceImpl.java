package org.healthystyle.health.service.sport.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.healthystyle.health.model.sport.Exercise;
import org.healthystyle.health.model.sport.Set;
import org.healthystyle.health.model.sport.Train;
import org.healthystyle.health.repository.sport.SetRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.dto.sport.SetSaveRequest;
import org.healthystyle.health.service.dto.sport.SetUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.error.sport.SetNotFoundException;
import org.healthystyle.health.service.error.sport.TrainNotFoundException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.sport.ExerciseService;
import org.healthystyle.health.service.sport.SetService;
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
public class SetServiceImpl implements SetService {
	@Autowired
	private SetRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private TrainService trainService;
	@Autowired
	private ExerciseService exerciseService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(SetServiceImpl.class);

	@Override
	public Set findById(Long id) throws ValidationException, SetNotFoundException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "set");
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("set.find.id.not_null", "Укажите идентификатор для поиска");
			throw new ValidationException("The id is null", result);
		}

		LOG.debug("Checking set for existence by id '{}'", id);
		Optional<Set> set = repository.findById(id);
		if (set.isEmpty()) {
			result.reject("set.find.not_found", "Не удалось найти подход");
			throw new SetNotFoundException(id, result);
		}
		LOG.info("Got set successfully by id '{}'", id);

		return set.get();
	}

	@Override
	public Page<Set> findByTrain(Long trainId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_TRAIN_PARAM_NAMES, trainId, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "set");
		if (trainId == null) {
			result.reject("set.find.train_id.not_null", "Укажите идентификатор тренировки для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Page<Set> sets = repository.findByTrain(trainId, PageRequest.of(page, limit));
		LOG.info("Got sets successfully by params: {}", params);

		return sets;
	}

	@Override
	public Page<Set> findByExercise(Long exerciseId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_EXERCISE_PARAM_NAMES, exerciseId, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "set");
		if (exerciseId == null) {
			result.reject("set.find.exercise_id.not_null", "Укажите идентификатор упражнения для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Page<Set> sets = repository.findByExercise(exerciseId, healthId, PageRequest.of(page, limit));
		LOG.info("Got sets successfully by params: {}", params);

		return sets;
	}

	@Override
	public Set save(SetSaveRequest saveRequest, Long trainId)
			throws ValidationException, TrainNotFoundException, ExerciseNotFoundException {
		LOG.debug("Validating set: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "set");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The set is invalid: %s. Result: %s", result, saveRequest, result);
		}

		Train train = trainService.findById(trainId);

		Long exerciseId = saveRequest.getExerciseId();
		Exercise exercise = exerciseService.findById(exerciseId);

		Set set = new Set(exercise, saveRequest.getCount(), saveRequest.getRepeat(), train);
		set = repository.save(set);
		LOG.info("The set was saved successfully: {}", set);

		return set;
	}

	@Override
	public void update(SetUpdateRequest updateRequest, Long id) throws ValidationException, SetNotFoundException {
		LOG.debug("Validating set: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "set");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The set is invalid: %s. Result: %s", result, updateRequest, result);
		}

		Set set = findById(id);

		Integer count = updateRequest.getCount();
		if (count != set.getCount()) {
			set.setCount(count);
		}

		Integer repeat = updateRequest.getRepeat();
		if (repeat != set.getRepeat()) {
			set.setRepeat(repeat);
		}

		repository.save(set);
		LOG.info("The set was updated successfully: {}", set);
	}

}
