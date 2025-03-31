package org.healthystyle.health.service.sport.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.healthystyle.health.model.sport.Exercise;
import org.healthystyle.health.model.sport.Step;
import org.healthystyle.health.repository.sport.StepRepository;
import org.healthystyle.health.service.dto.sport.StepSaveRequest;
import org.healthystyle.health.service.dto.sport.StepUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.error.sport.StepNotFoundException;
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
public class StepServiceImpl implements StepService {
	@Autowired
	private StepRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private ExerciseService exerciseService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(StepServiceImpl.class);

	@Override
	public Step findById(Long id) throws ValidationException, StepNotFoundException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "step");
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("step.find.id.not_null", "Укажите идентификатор для поиска");
			throw new ValidationException("The id is null", result);
		}

		LOG.debug("Checking step for existence by id '{}'", id);
		Optional<Step> step = repository.findById(id);
		if (step.isEmpty()) {
			result.reject("step.find.not_found", "Не удалось найти шаг");
			throw new StepNotFoundException(id, result);
		}
		LOG.info("Got step successfully by id '{}'", id);

		return step.get();
	}

	@Override
	public Page<Step> findByExercise(Long id, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_EXERCISE_PARAM_NAMES, id, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "step");
		if (id == null) {
			result.reject("step.find.exercise_id.not_null", "Укажите упражнение для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Page<Step> steps = repository.findByExercise(id, PageRequest.of(page, limit));
		LOG.info("Got steps successfully by params '{}'", params);

		return steps;
	}

	@Override
	public Step save(StepSaveRequest saveRequest, Long exerciseId)
			throws ValidationException, ExerciseNotFoundException {
		LOG.debug("Validating step: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "step");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The step is invalid: %s. Result: %s", result, saveRequest, result);
		}
		
		Exercise exercise = exerciseService.findById(exerciseId);
		
		Step step = new Step(saveRequest.getDescription(), exercise);
		step = repository.save(step);
		LOG.info("The step '{}' was saved successfully", step);
		
		return step;
	}

	@Override
	public void update(StepUpdateRequest updateRequest, Long id) throws ValidationException, StepNotFoundException {
		LOG.debug("Validating step: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "step");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The step is invalid: %s. Result: %s", result, updateRequest, result);
		}
		
		Step step = findById(id);
		
		String description = updateRequest.getDescription();
		if(!description.equals(step.getDescription())) {
			step.setDescription(description);
			repository.save(step);
			LOG.info("The step '{}' was update successfully", step);
		}
	}

}
