package org.healthystyle.health.service.medicine.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.medicine.Plan;
import org.healthystyle.health.model.medicine.Treatment;
import org.healthystyle.health.repository.medicine.TreatmentRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.medicine.IntakeExistException;
import org.healthystyle.health.service.error.medicine.MedicineNotFoundException;
import org.healthystyle.health.service.error.medicine.PlanOverlapsException;
import org.healthystyle.health.service.error.medicine.TreatmentExistException;
import org.healthystyle.health.service.error.medicine.TreatmentNotFoundException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.medicine.PlanService;
import org.healthystyle.health.service.medicine.TreatmentService;
import org.healthystyle.health.service.medicine.dto.PlanSaveRequest;
import org.healthystyle.health.service.medicine.dto.TreatmentSaveRequest;
import org.healthystyle.health.service.medicine.dto.TreatmentUpdateRequest;
import org.healthystyle.health.service.validation.ParamsChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

@Service
public class TreatmentServiceImpl implements TreatmentService {
	@Autowired
	private TreatmentRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private PlanService planService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(TreatmentServiceImpl.class);

	@Override
	public Treatment findById(Long id) throws ValidationException, TreatmentNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "treatment");

		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("treatment.find.id.not_null", "Укажите идентификатор для поиска");
			throw new ValidationException("Exception occurred while fetching treatment by id. The id is null", result);
		}

		LOG.debug("Checking treatment for existence by id '{}'", id);
		Optional<Treatment> treatment = repository.findById(id);
		if (treatment.isEmpty()) {
			result.reject("treatment.find.id.not_found", "Не удалось найти лечение по заданному идентификатору");
			throw new TreatmentNotFoundException(id, result);
		}
		LOG.info("Got treatment successfully by id '{}'", id);

		return treatment.get();
	}

	@Override
	public Page<Treatment> findByDescription(String description, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_DESCRIPTION_PARAM_NAMES, description, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "treatment");
		if (description == null || description.trim().isEmpty()) {
			result.reject("treatment.find.description.not_null", "Укажите описание лечения для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching treatments. The params are invalid: %s. Result: %s", result,
					params, result);
		}

		LOG.debug("The params are OK: {}", params);

		LOG.debug("Getting health to get treatments by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Treatment> treatments = repository.findByDescription(description, health.getId(),
				PageRequest.of(page, limit));
		LOG.info("Got treatments successfully by params: {}", params);

		return treatments;
	}

	@Override
	public Page<Treatment> find(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PARAM_NAMES, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "treatment");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching treatments. The params are invalid: %s. Result: %s", result,
					params, result);
		}

		LOG.debug("The params are OK: {}", params);

		LOG.debug("Getting health to get treatments by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Treatment> treatments = repository.find(health.getId(), PageRequest.of(page, limit));
		LOG.info("Got treatments successfully by params: {}", params);

		return treatments;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Treatment save(TreatmentSaveRequest saveRequest) throws ValidationException, TreatmentExistException,
			TreatmentNotFoundException, MedicineNotFoundException, PlanOverlapsException, IntakeExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
		LOG.debug("Validating treatments: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "treatment");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while saving a treatment. The treatment is invalid: %s. Result: %s", result,
					saveRequest, result);
		}

		LOG.debug("Getting health to save treatment: {}", saveRequest);
		Health health = healthAccessor.getHealth();

		String description = saveRequest.getDescription();
		LOG.debug("Checking treatment for existence by description '{}'", description);
		if (repository.existsByDescription(description.toLowerCase(), health.getId())) {
			result.reject("treatment.save.description.exists", "Лечение с данным описанием уже существует");
			throw new TreatmentExistException(description, result);
		}

		LOG.debug("The treatment is OK: {}", saveRequest);

		Treatment treatment = new Treatment(description, health);
		treatment = repository.save(treatment);
		LOG.info("Treatment was saved successfully: {}", treatment);

		List<PlanSaveRequest> planSaveRequests = saveRequest.getPlans();
		for (PlanSaveRequest planSaveRequest : planSaveRequests) {
			Plan plan = planService.save(planSaveRequest, treatment.getId());
			treatment.addPlans(plan);
		}

		return treatment;
	}

	@Override
	public void update(TreatmentUpdateRequest updateRequest, Long id)
			throws ValidationException, TreatmentNotFoundException, TreatmentExistException {
		LOG.debug("Validating treatments: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "treatment");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while updating a treatment. The treatment is invalid: %s. Result: %s", result,
					updateRequest, result);
		}

		LOG.debug("Checking treatment for existence by id '{}'", id);
		Treatment treatment = findById(id);

		LOG.debug("Getting health to update treatment: {}", updateRequest);
		Health health = healthAccessor.getHealth();

		String description = updateRequest.getDescription();
		if (!treatment.getDescription().equals(description)) {
			LOG.debug("Checking treatment for existence by description '{}'", description);
			if (repository.existsByDescription(description.toLowerCase(), health.getId())) {
				result.reject("treatment.save.description.exists", "Лечение с данным описанием уже существует");
				throw new TreatmentExistException(description, result);
			}
			treatment.setDescription(description);
		}

		LOG.debug("The treatment is OK: {}", updateRequest);

		treatment = repository.save(treatment);
		LOG.info("Treatment was updated successfully: {}", treatment);
	}

	@Override
	public void deleteById(Long id) throws ValidationException, TreatmentNotFoundException {
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "treatment");
			result.reject("treatment.delete.id.not_null", "Укажите идентификатор для удаления");
			throw new ValidationException("Exception occurred while deleting a treatment by id. The id is null",
					result);
		}

		LOG.debug("Checking treatment for existence by id '{}'", id);
		Treatment treatment = findById(id);

		repository.delete(treatment);
		LOG.info("The treatment was deleted successfully by id '{}'", id);
	}

}
