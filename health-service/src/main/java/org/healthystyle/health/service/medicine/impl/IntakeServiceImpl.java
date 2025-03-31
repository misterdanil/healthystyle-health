package org.healthystyle.health.service.medicine.impl;

import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.model.medicine.Intake;
import org.healthystyle.health.model.medicine.Plan;
import org.healthystyle.health.repository.medicine.IntakeRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.medicine.IntakeExistException;
import org.healthystyle.health.service.error.medicine.IntakeNotFoundException;
import org.healthystyle.health.service.error.medicine.PlanNotFoundException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.measure.MeasureService;
import org.healthystyle.health.service.measure.convert.ConvertTypeService;
import org.healthystyle.health.service.medicine.IntakeService;
import org.healthystyle.health.service.medicine.PlanService;
import org.healthystyle.health.service.medicine.dto.IntakeSaveRequest;
import org.healthystyle.health.service.medicine.dto.IntakeUpdateRequest;
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
public class IntakeServiceImpl implements IntakeService {
	@Autowired
	private IntakeRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private PlanService planService;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private ConvertTypeService convertTypeService;
	@Autowired
	private MeasureService measureService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(IntakeServiceImpl.class);

	@Override
	public Intake findById(Long id) throws ValidationException, IntakeNotFoundException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "intake");

		LOG.debug("Checking id for not null");
		if (id == null) {
			result.reject("intake.find.id.not_null", "Укажите идентификатор для поиска приёма лекарств");
			throw new ValidationException("Exception occurred while fetching intake by id. The id is null", result);
		}

		Optional<Intake> intake = repository.findById(id);
		LOG.debug("Checking intake for existence by id '{}'", id);
		if (intake.isEmpty()) {
			result.reject("intake.find.not_exists", "Не удалось найти приём лекарств");
			throw new IntakeNotFoundException(id, result);
		}

		return intake.get();
	}

	@Override
	public Page<Intake> findByMedicine(String name, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_MEDICINE_PARAM_NAMES, name, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "intake");
		if (name == null || name.isBlank()) {
			result.reject("intake.find.name.not_empty", "Укажите название лекарства для поиска приёмов лекарств");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching intakes. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get intakes: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Intake> intakes = repository.findByMedicine(name, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got intakes successfully by params: {}", params);

		return intakes;
	}

	@Override
	public Page<Intake> findByPlan(Long planId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_PLAN_PARAM_NAMES, planId, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "intake");
		if (planId == null) {
			result.reject("intake.find.plan_id.not_null", "Укажите идентификатор плана для поиска приёмов лекарств");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching intakes. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		Page<Intake> intakes = repository.findByPlan(planId, PageRequest.of(page, limit));
		LOG.info("Got intakes successfully by params: {}", params);

		return intakes;
	}

	@Override
	public Page<Intake> findByDay(Integer day, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_DAY_PARAM_NAMES, day, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "intake");
		if (day == null) {
			result.reject("intake.find.day.not_null", "Укажите день для поиска приёмов лекарств");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching intakes. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get intakes: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Intake> intakes = repository.findByDay(day, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got intakes successfully by params: {}", params);

		return intakes;
	}

	@Override
	public List<Intake> findByDate(Instant date, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_DATE_PARAM_NAMES, date, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "intake");
		if (date == null) {
			result.reject("intake.find.date.not_null", "Укажите дату для поиска приёмов лекарств");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching intakes. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get intakes: {}", params);
		Health health = healthAccessor.getHealth();

		List<Intake> intakes = repository.findByDate(date, health.getId(), page, limit);
		LOG.info("Got intakes successfully by params: {}", params);

		return intakes;
	}

	@Override
	public List<Intake> findByCurrentDate(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_CURRENT_DATE_PARAM_NAMES, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "intake");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching intakes. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get intakes: {}", params);
		Health health = healthAccessor.getHealth();

		List<Intake> intakes = repository.findByCurrentDate(health.getId(), page, limit);
		LOG.info("Got intakes successfully by params: {}", params);

		return intakes;
	}

	@Override
	public List<Intake> findPlanned(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PLANNED_PARAM_NAMES, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "intake");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching intakes. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get intakes: {}", params);
		Health health = healthAccessor.getHealth();

		List<Intake> intakes = repository.findPlanned(health.getId(), page, limit);
		LOG.info("Got intakes successfully by params: {}", params);

		return intakes;
	}

	@Override
	public List<Intake> findNextIntake(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_NEXT_INTAKE_PARAM_NAMES, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "intake");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching intakes. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get intakes: {}", params);
		Health health = healthAccessor.getHealth();

		List<Intake> intakes = repository.findNextIntake(health.getId(), page, limit);
		LOG.info("Got intakes successfully by params: {}", params);

		return intakes;
	}

	@Override
	public Intake save(IntakeSaveRequest saveRequest, Long planId)
			throws ValidationException, PlanNotFoundException, IntakeExistException, WeightNegativeOrZeroException,
			ConvertTypeNotRecognizedException, MeasureNotFoundException {
		LOG.debug("Validating intake '{}' and plane id '{}'", saveRequest, planId);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "intake");
		validator.validate(saveRequest, result);
		String weight = saveRequest.getWeight();
		Type measureType = saveRequest.getMeasureType();
		if (weight != null && measureType == null) {
			result.rejectValue("measure", "intake.save.measure.not_null", "Укажите единицу измерения для веса");
		}
		if (planId == null) {
			result.reject("intake.save.plan_id.not_null",
					"Укажите идентификатор плана для добавления приёма лекарства");
		}
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while saving an intake. The data is invalid. Intake: %s. Plan id: %s", result,
					saveRequest, planId);
		}

		LOG.debug("Checking plan for existence by id '{}'", planId);
		Plan plan = planService.findById(planId);

		LOG.debug("The data is valid. Intake: {}. Plan id: {}", saveRequest, planId);

		LOG.debug("Truncing time to minutes: {}", saveRequest);
		LocalTime time = saveRequest.getTime().truncatedTo(ChronoUnit.MINUTES);
		LOG.debug("Time from '{}' to '{}'", saveRequest.getTime(), time);

		Integer day = saveRequest.getDay();

		if (repository.existsByTimeAndDayAndPlanId(time, day, planId)) {
			result.reject("intake.save.exists", "Уже существует приём лекарства с данными временными параметрами");
			throw new IntakeExistException(time, day, planId, result);
		}

		Intake intake = new Intake(plan, time, day);

		if (weight != null) {
			LOG.debug("Recognizing weight convert type: {}", saveRequest);
			ConvertType convertType = ParamsChecker.checkConvertType(weight, convertTypeService, result);

			LOG.debug("Checking measure for existence by type '{}'", measureType);
			Measure measure = measureService.findByType(measureType);
			intake.setWeight(weight);
			intake.setMeasure(measure);
			intake.setConvertType(convertType);
		}

		repository.save(intake);
		LOG.info("Intake was saved successfully: {}", intake);

		return intake;
	}

	@Override
	public void update(IntakeUpdateRequest updateRequest, Long intakeId)
			throws ValidationException, IntakeNotFoundException, MeasureNotFoundException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, IntakeExistException {
		LOG.debug("Validating intake '{}' and intake id '{}'", updateRequest, intakeId);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "intake");
		validator.validate(updateRequest, result);
		String weight = updateRequest.getWeight();
		Type measureType = updateRequest.getMeasure();
		if (weight != null && measureType == null) {
			result.rejectValue("measure", "intake.save.measure.not_null", "Укажите единицу измерения для веса");
		}
		if (intakeId == null) {
			result.reject("intake.update.id.not_null", "Укажите идентификатор приёма лекарств для обновления");
		}
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while updating intake. The data is invalid. Intake: %s. Intake id: %s", result,
					updateRequest, intakeId);
		}

		LOG.debug("Checking intake for existence by id '{}'", intakeId);
		Intake intake = findById(intakeId);

		LOG.debug("The data is valid. Intake: {}. Intake id: {}", updateRequest, intakeId);

		LocalTime time = updateRequest.getTime().truncatedTo(ChronoUnit.MINUTES);
		Integer day = updateRequest.getDay();
		if (!time.equals(intake.getTime()) || day != intake.getDay()) {
			Long planId = intake.getPlan().getId();
			if (repository.existsByTimeAndDayAndPlanId(time, day, planId)) {
				result.reject("intake.update.exists",
						"С данными временными параметрами уже существуем приём лекарства в данном плане");
				throw new IntakeExistException(time, day, planId, result);
			}
			intake.setTime(time);
			intake.setDay(day);
		}

		if (weight != null && !weight.equals(intake.getWeight())) {
			LOG.debug("Recognizing weight convert type '{}' for intake '{}'", updateRequest, intakeId);
			ConvertType convertType = ParamsChecker.checkConvertType(weight, convertTypeService, result);

			intake.setWeight(weight);
			intake.setConvertType(convertType);
		} else if (weight == null) {
			intake.setWeight(null);
			intake.setConvertType(null);
			intake.setMeasure(null);
		}

		Measure measure = intake.getMeasure();
		if (weight != null && (measure == null || !measureType.equals(measure.getType()))) {
			LOG.debug("Checking measure for existence by type '{}' for intake '{}'", measureType, intakeId);
			measure = measureService.findByType(measureType);
			intake.setMeasure(measure);
		}

		repository.save(intake);
		LOG.info("The intake was updated successfully: {}", intake);
	}

	@Override
	public void deleteById(Long id) throws ValidationException, IntakeNotFoundException {
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "intake");
			result.reject("intake.delete.id.not_null", "Укажите идентификатор для удаления");
			throw new ValidationException("Exception occurred while deleting intake by id. The id is null", result);
		}

		LOG.debug("Checking intake for existence by id '{}'", id);
		Intake intake = findById(id);

		repository.delete(intake);
		LOG.info("Intake was deleted successfully: {}", intake);
	}

}
