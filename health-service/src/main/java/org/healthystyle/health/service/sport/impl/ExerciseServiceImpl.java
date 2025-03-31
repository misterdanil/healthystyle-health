package org.healthystyle.health.service.sport.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.sport.Exercise;
import org.healthystyle.health.model.sport.Step;
import org.healthystyle.health.repository.sport.ExerciseRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.dto.sport.ExerciseSaveRequest;
import org.healthystyle.health.service.dto.sport.ExerciseUpdateRequest;
import org.healthystyle.health.service.dto.sport.StepSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseExistException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.sport.ExerciseService;
import org.healthystyle.health.service.sport.StepService;
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
public class ExerciseServiceImpl implements ExerciseService {
	@Autowired
	private ExerciseRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private StepService stepService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(ExerciseServiceImpl.class);

	@Override
	public Exercise findById(Long id) throws ValidationException, ExerciseNotFoundException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "exercise");
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("exercise.find.id.not_null", "Укажите идентификатор для поиска");
			throw new ValidationException("The id is null", result);
		}

		LOG.debug("Checking exercise for existence by id '{}'", id);
		Optional<Exercise> exercise = repository.findById(id);
		if (exercise.isEmpty()) {
			result.reject("exercise.find.not_found", "Не удалось найти упражнение");
			throw new ExerciseNotFoundException(id, result);
		}
		LOG.info("Got exercise successfully by id '{}'", id);

		return exercise.get();
	}

	@Override
	public Page<Exercise> findByTitle(String title, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_TITLE_PARAM_NAMES, title, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "exercise");
		if (title == null || title.isBlank()) {
			result.reject("exercise.find.title.not_blank", "Укажите название для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Page<Exercise> exercises = repository.findByTitle(title, healthId, PageRequest.of(page, limit));
		LOG.info("Got exercises successfully by params '{}'", params);

		return exercises;
	}

	@Override
	public Page<Exercise> find(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PARAM_NAMES, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "exercise");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Page<Exercise> exercises = repository.find(healthId, PageRequest.of(page, limit));
		LOG.info("Got exercises successfully by params '{}'", params);

		return exercises;
	}

	@Override
	public Exercise save(ExerciseSaveRequest saveRequest) throws ValidationException, ExerciseExistException, ExerciseNotFoundException {
		LOG.debug("Validating exercise: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "exercise");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The exercise is invalid: %s. Result: %s", result, saveRequest, result);
		}

		Health health = healthAccessor.getHealth();
		Long healthId = health.getId();

		String title = saveRequest.getTitle();
		LOG.debug("Checking exercise for existence by title '{}'", title);
		if (repository.existsByTitle(title, healthId)) {
			result.reject("exercise.find.existed", "Упражнение с таким названием уже у вас существует");
			throw new ExerciseExistException(title, healthId, result);
		}

		LOG.debug("The exercise is OK: {}", saveRequest);

		Exercise exercise = new Exercise(title, health);
		exercise = repository.save(exercise);
		LOG.info("Exercise was saved successfully: {}", exercise);

		List<StepSaveRequest> stepSaveRequests = saveRequest.getSteps();
		LOG.debug("Saving steps: {}", stepSaveRequests);
		for (StepSaveRequest stepSaveRequest : stepSaveRequests) {
			Step step = stepService.save(stepSaveRequest, exercise.getId());
			exercise.addStep(step);
		}

		return exercise;
	}

	@Override
	public boolean existsById(Long id) throws ValidationException {
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "exercise");
			result.reject("exercise.exists.id.not_null", "Укажите идентификатор для проверки");
			throw new ValidationException("The id is null", result);
		}
		
		boolean exists = repository.existsById(id);
		LOG.info("Got result '{}' successfully", exists);
		
		return exists;
	}

	@Override
	public void update(ExerciseUpdateRequest updateRequest, Long id)
			throws ValidationException, ExerciseNotFoundException, ExerciseExistException {
		LOG.debug("Validating exercise: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "exercise");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The exercise is invalid: %s. Result: %s", result, updateRequest, result);
		}

		Exercise exercise = findById(id);

		Health health = healthAccessor.getHealth();
		Long healthId = health.getId();

		String title = updateRequest.getTitle().trim();
		if (!title.equals(exercise.getTitle())) {
			LOG.debug("Checking exercise for existence by title '{}'", title);
			if (repository.existsByTitle(title, healthId)) {
				result.reject("exercise.find.existed", "Упражнение с таким названием уже у вас существует");
				throw new ExerciseExistException(title, healthId, result);
			} else {
				exercise.setTitle(title);
			}
		}

		repository.save(exercise);
		LOG.info("Exercise was updated successfully: {}", exercise);
	}

}
