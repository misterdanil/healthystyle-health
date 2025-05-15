package org.healthystyle.health.service.medicine.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.medicine.Intake;
import org.healthystyle.health.model.medicine.Result;
import org.healthystyle.health.repository.medicine.ResultRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.medicine.IntakeNotFoundException;
import org.healthystyle.health.service.error.medicine.ResultExistException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.medicine.IntakeService;
import org.healthystyle.health.service.medicine.ResultService;
import org.healthystyle.health.service.medicine.dto.ResultSaveRequest;
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

@Service("intake_result_service_impl")
public class ResultServiceImpl implements ResultService {
	@Autowired
	private ResultRepository repository;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private IntakeService intakeService;
	@Autowired
	private Validator validator;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(ResultServiceImpl.class);

	@Override
	public Page<Result> findByIntake(Long intakeId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_INTAKE_PARAM_NAMES, intakeId, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (intakeId == null) {
			result.reject("result.find.intake_id.not_null",
					"Укажите идентификатор приёма лекарства для поиска результатов");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching results by intake '%s'. The params are invalid: %s. Result: %s",
					result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<Result> results = repository.findByIntake(intakeId, PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<Result> findByPlan(Long planId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_PLAN_PARAM_NAMES, planId, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (planId == null) {
			result.reject("result.find.intake_id.not_null",
					"Укажите идентификатор приёма лекарства для поиска результатов");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching results by intake '%s'. The params are invalid: %s. Result: %s",
					result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<Result> results = repository.findByPlan(planId, PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<Result> find(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PARAM_NAMES, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching results by intake '%s'. The params are invalid: %s. Result: %s",
					result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		LOG.debug("Getting health to get results by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Result> results = repository.find(health.getId(), PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Result save(ResultSaveRequest saveRequest, Long intakeId) throws ValidationException, ResultExistException, IntakeNotFoundException {
		LOG.debug("Validating result: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "result");
		validator.validate(saveRequest, result);

		LOG.debug("Checking intake id for not null: {}", intakeId);
		if (intakeId == null) {
			result.reject("result.save.intake_id.not_null",
					"Укажите идентификатор приёма лекарства для добавления результат");
			throw new ValidationException("Exception occurred while saving result. The intake id is null", result);
		}

		LOG.debug("Checking intake for existence by id '{}'", intakeId);
		Intake intake = intakeService.findById(intakeId);

		LocalDate date = saveRequest.getDate();
		LOG.debug("Checking result for existence by intake id '{}' and date '{}'", intakeId, date);
		if (repository.existsByIntakeAndDate(intakeId, date)) {
			result.reject("result.save.exists", "Результат у данного приёма лекарства в этот день уже есть");
			throw new ResultExistException(intakeId, date, result);
		}

		LOG.debug("The result is OK: {}", saveRequest);

		Result intakeResult = new Result(intake, saveRequest.getDate());
		repository.save(intakeResult);
		LOG.info("The result was saved successfully: {}", intakeResult);

		return intakeResult;
	}

	@Override
	public void deleteById(Long id) throws ValidationException {
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "result");
			result.reject("result.delete.id.not_null", "Укажите идентификатор для удаления");
			throw new ValidationException("Exception occurred while deleting result. The id is null", result);
		}

		repository.deleteById(id);
		LOG.info("The result was deleted successfully by id '{}'", id);
	}

}
