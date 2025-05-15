package org.healthystyle.health.service.medicine.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.medicine.Intake;
import org.healthystyle.health.model.medicine.Medicine;
import org.healthystyle.health.model.medicine.Plan;
import org.healthystyle.health.model.medicine.Sequence;
import org.healthystyle.health.model.medicine.Treatment;
import org.healthystyle.health.repository.medicine.PlanRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.medicine.IntakeExistException;
import org.healthystyle.health.service.error.medicine.MedicineNotFoundException;
import org.healthystyle.health.service.error.medicine.PlanNotFoundException;
import org.healthystyle.health.service.error.medicine.PlanOverlapsException;
import org.healthystyle.health.service.error.medicine.TreatmentNotFoundException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.medicine.IntakeService;
import org.healthystyle.health.service.medicine.MedicineService;
import org.healthystyle.health.service.medicine.PlanService;
import org.healthystyle.health.service.medicine.TreatmentService;
import org.healthystyle.health.service.medicine.dto.IntakeSaveRequest;
import org.healthystyle.health.service.medicine.dto.PlanSaveRequest;
import org.healthystyle.health.service.medicine.dto.PlanUpdateRequest;
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
public class PlanServiceImpl implements PlanService {
	@Autowired
	private PlanRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private IntakeService intakeService;
	@Autowired
	private MedicineService medicineService;
//	@Autowired
//	private ConvertTypeService convertTypeService;
//	@Autowired
//	private MeasureService measureService;
	@Autowired
	private TreatmentService treatmentService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(PlanServiceImpl.class);

	@Override
	public Plan findById(Long id) throws ValidationException, PlanNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "plan");

		LOG.debug("Checking id for nul null: {}", id);
		if (id == null) {
			result.reject("plan.find.id.not_null", "Укажите идентификатор плана для поиска");
			throw new ValidationException("Exception occurred while fetching plan by id. The id is null", result);
		}

		LOG.debug("Checking plan for existence by id '{}'", id);
		Optional<Plan> plan = repository.findById(id);
		if (plan.isEmpty()) {
			result.reject("plan.find.not_found", "Не удалось найти план приёма лекарств");
			throw new PlanNotFoundException(id, result);
		}
		LOG.debug("Got plan by id '{}' successfully", id);

		return plan.get();
	}

	@Override
	public Page<Plan> findByMedicine(Long medicineId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_MEDICINE_PARAM_NAMES, medicineId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "plan");
		LOG.debug("Validating params: {}", params);
		if (medicineId == null) {
			result.reject("plan.find.medicine_id.not_null", "Укажите идентификатор лекарства для поиска плана");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching plans. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get plans by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Plan> plans = repository.findByMedicine(medicineId, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got plans successfully by params '{}'", params);

		return plans;
	}

	@Override
	public Page<Plan> findActual(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_ACTUAL_PARAM_NAMES, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "plan");
		LOG.debug("Validating params: {}", params);
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching plans. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get plans by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Plan> plans = repository.findActual(health.getId(), PageRequest.of(page, limit));
		LOG.info("Got plans successfully by params '{}'", params);

		return plans;
	}

	@Override
	public Page<Plan> find(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PARAM_NAMES, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "plan");
		LOG.debug("Validating params: {}", params);
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching plans. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get plans by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Plan> plans = repository.find(health.getId(), PageRequest.of(page, limit));
		LOG.info("Got plans successfully by params '{}'", params);

		return plans;
	}

	@Override
	public Page<Plan> findByTreatment(Long treatmentId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_TREATMENT_PARAM_NAMES, treatmentId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "plan");
		LOG.debug("Validating params: {}", params);
		if (treatmentId == null) {
			result.reject("plan.find.treatment_id.not_null", "Укажите идентификатор лечения для поиска планов");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching plans. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get plans by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Plan> plans = repository.findByTreatment(treatmentId, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got plans successfully by params '{}'", params);

		return plans;
	}

	@Override
	public Plan save(PlanSaveRequest saveRequest, Long treatmentId) throws ValidationException,
			TreatmentNotFoundException, MedicineNotFoundException, PlanOverlapsException, IntakeExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
		LOG.debug("Validating plan: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "plan");
		validator.validate(saveRequest, result);
//		String weight = saveRequest.getWeight();
//		Type measureType = saveRequest.getMeasure();
//		if (weight != null && measureType == null) {
//			result.rejectValue("measure", "plan.save.measure.not_null", "Укажите единицу измерения для веса");
//		}
		if (result.hasErrors()) {
			throw new ValidationException("Exception occurred while saving plan. The plan is invalid: %s. Result: %s",
					result, saveRequest, result);
		}

		LOG.debug("Checking treatment for existence by id '%s'", treatmentId);
		Treatment treatment = treatmentService.findById(treatmentId);

		Long medicineId = saveRequest.getMedicineId();
		LOG.debug("Checking medicine for existence by id: '{}'", saveRequest);
		Medicine medicine = medicineService.findById(medicineId);

		LOG.debug("Checking dates overlaps by medicine, start and end: {}", saveRequest);
		LocalDate start = saveRequest.getStart();
		LocalDate end = saveRequest.getEnd();
		if (repository.existsOverlaps(start, end, medicineId, null)) {
			result.reject("plan.save.overlaps",
					"Уже существует план приёма лекарства, который пересекается с этими датами");
			throw new PlanOverlapsException(start, end, medicineId, result);
		}

		LOG.debug("Getting health to save plan: {}", saveRequest);
		Health health = healthAccessor.getHealth();

		Plan plan = new Plan(medicine, start, end, treatment, health);
		plan = repository.save(plan);
		Long planId = plan.getId();

		List<IntakeSaveRequest> intakes = saveRequest.getIntakes();
		LOG.debug("Saving intakes '{}' for plan '{}'", saveRequest, planId);
		for (IntakeSaveRequest intake : intakes) {
			LOG.debug("Saving intake '{}' for plan '{}'", intake, planId);
			try {
				Intake savedIntake = intakeService.save(intake, planId);
				plan.addIntake(savedIntake);
			} catch (PlanNotFoundException e) {
				throw new RuntimeException("Plan was saved during transaction but the intake couldn't find it", e);
			}
		}

//		if (weight != null) {
//			LOG.debug("Recognizing weight convert type: {}", weight);
//			ConvertType convertType = convertTypeService.getConvertTypeByValue(weight);
//			if ((convertType instanceof IntegerNumber && Integer.valueOf(weight) <= 0)
//					|| (convertType instanceof FloatNumber && Float.valueOf(weight) <= 0)) {
//				result.rejectValue("weight", "plan.save.weight.negative_or_zero",
//						"Вес принимаемого лекарства должен быть больше нуля");
//				throw new WeightNegativeOrZeroException(weight, result);
//			}
//
//			LOG.debug("Checking measure for existence by type '{}'", measureType);
//			Measure measure = measureService.findByType(measureType);
//			plan.setWeight(weight);
//			plan.setMeasure(measure);
//			plan.setConvertType(convertType);
//		}

		Sequence sequence = saveRequest.getSequence();
		if (sequence != null) {
			plan.setSequence(sequence);
		}

		LOG.debug("The data is OK. Plan: {}. Treatment id: {}", saveRequest, treatmentId);

		plan = repository.save(plan);
		LOG.info("The plan was saved successfully: {}", plan);

		return plan;
	}

	@Override
	public void update(PlanUpdateRequest updateRequest, Long id)
			throws ValidationException, PlanNotFoundException, PlanOverlapsException {
		LOG.debug("Validatin plan '{}' and plan id '{}'", updateRequest, id);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "plan");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while updating plan. The data is invalid. Plan: %s. Id: %s. Result: %s", result,
					updateRequest, id, result);
		}

		LOG.debug("Checking plan for existence by id '{}'", id);
		Plan plan = findById(id);

		LocalDate start = updateRequest.getStart();
		LocalDate end = updateRequest.getEnd();
		if (!start.equals(plan.getStart()) || !end.equals(plan.getEnd())) {
			LOG.debug("Checking date overlaps for plan '{}': {}", id, updateRequest);
			Long medicineId = plan.getMedicine().getId();
			if (repository.existsOverlaps(start, end, plan.getMedicine().getId(), id)) {
				result.reject("plan.update.overlaps",
						"Уже существует план приёма лекарства, пересекающийся с данными датами");
				throw new PlanOverlapsException(start, end, medicineId, result);
			}
			plan.setStart(start);
			plan.setEnd(end);
		}

		Sequence sequence = updateRequest.getSequence();
		plan.setSequence(sequence);

		LOG.debug("The data is OK. Plan: {}. Id: {}", updateRequest, id);

		repository.save(plan);
		LOG.info("The plan was updated successfully: {}", plan);
	}

	@Override
	public void deleteById(Long id) throws ValidationException, PlanNotFoundException {
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "plan");
			result.reject("plan.delete.id.not_null", "Укажите идентификатор для удаления");
			throw new ValidationException("Exception occurred while deleteing plan by id. The id is null", result);
		}

		LOG.debug("Checking plan for existence by id '{}'", id);
		Plan plan = findById(id);

		repository.delete(plan);
		LOG.info("Plan was deleted successfully by id '{}'", id);
	}

}
