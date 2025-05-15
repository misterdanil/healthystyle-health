package org.healthystyle.health.service.medicine.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.model.medicine.Medicine;
import org.healthystyle.health.repository.medicine.MedicineRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.medicine.MedicineExistException;
import org.healthystyle.health.service.error.medicine.MedicineNotFoundException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.measure.MeasureService;
import org.healthystyle.health.service.measure.convert.ConvertTypeService;
import org.healthystyle.health.service.medicine.MedicineService;
import org.healthystyle.health.service.medicine.dto.MedicineSaveRequest;
import org.healthystyle.health.service.medicine.dto.MedicineUpdateRequest;
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
public class MedicineServiceImpl implements MedicineService {
	@Autowired
	private MedicineRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private MeasureService measureService;
	@Autowired
	private ConvertTypeService convertTypeService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(MedicineServiceImpl.class);

	@Override
	public Medicine findById(Long id) throws ValidationException, MedicineNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "medicine");

		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("medicine.find.id.not_null", "Укажите идентификатор для поиска");
			throw new ValidationException("Exception occurred while fetching medicine by id. The id is null", result);
		}

		LOG.debug("Checking medicine for existence by id '{}'", id);
		Optional<Medicine> medicine = repository.findById(id);
		if (medicine.isEmpty()) {
			result.reject("medicine.find.not_found", "Не удалось найти лекарство");
			throw new MedicineNotFoundException(id, result);
		}
		LOG.info("Got medicine by id '{}' successfully", id);

		return medicine.get();
	}

	@Override
	public Page<Medicine> findByName(String name, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_NAME_PARAM_NAMES, name, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "medicine");
		LOG.debug("Validating params: {}", params);
		if (name == null) {
			result.reject("medicine.find.name.not_null", "Укажите название лекарства для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching medicines. The params are invalid: %s. Result: %s", result,
					params, result);
		}

		LOG.debug("The params are OK: {}", params);

		LOG.debug("Getting health to get medicines by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Medicine> medicines = repository.findByName(name, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got medicines by params '{}' successfully", params);

		return medicines;
	}

	@Override
	public Page<Medicine> find(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PARAM_NAMES, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "medicine");
		LOG.debug("Validating params: {}", params);
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching medicines. The params are invalid: %s. Result: %s", result,
					params, result);
		}

		LOG.debug("The params are OK: {}", params);

		LOG.debug("Getting health to get medicines by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Medicine> medicines = repository.find(health.getId(), PageRequest.of(page, limit));
		LOG.info("Got medicines by params '{}' successfully", params);

		return medicines;
	}

	@Override
	public Medicine save(MedicineSaveRequest saveRequest) throws ValidationException, MedicineExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
		LOG.debug("Validating medicine: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "medicine");
		validator.validate(saveRequest, result);

		String weight = saveRequest.getWeight();
		Type measureType = saveRequest.getMeasure();
		if (weight != null && measureType == null) {
			result.reject("medicine.save.measure.not_null", "Укажите измерение для веса лекарства");
		}
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while saving medicine. The medicine is invalid: %s. Result: %s", result,
					saveRequest, result);
		}

		LOG.debug("Getting health to save medicine: {}", saveRequest);
		Health health = healthAccessor.getHealth();

		LOG.debug("Checking name for existence: {}", saveRequest);
		String name = saveRequest.getName();
		if (repository.existsByName(name, health.getId())) {
			result.reject("medicine.save.exists", "Лекарство с таким названием уже существует");
			throw new MedicineExistException(name, result);
		}

		Medicine medicine = new Medicine(name, health);

		if (weight != null) {
			medicine.setWeight(weight);
			
			LOG.debug("Recognizing a convert type for weight: {}", saveRequest);
			ConvertType convertType = ParamsChecker.checkConvertType(weight, convertTypeService, result);
			medicine.setConvertType(convertType);

			LOG.debug("Checking measure for existence by type: {}", saveRequest);
			Measure measure = measureService.findByType(measureType);
			medicine.setMeasure(measure);
		}

		LOG.debug("The medicine is OK: {}", saveRequest);

		medicine = repository.save(medicine);
		LOG.info("The medicine was saved successfully: {}", medicine);

		return medicine;
	}

	@Override
	public void update(MedicineUpdateRequest updateRequest, Long id)
			throws ValidationException, MedicineNotFoundException, WeightNegativeOrZeroException,
			ConvertTypeNotRecognizedException, MeasureNotFoundException {
		LOG.debug("Validating medicine '{}' and id '{}'", updateRequest, id);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "medicine");
		validator.validate(updateRequest, result);

		LOG.debug("Checking id for not null");
		if (id == null) {
			result.reject("medicine.update.id.not_null", "Укажите идентификатор лекарства для обновления");
		}

		String weight = updateRequest.getWeight();
		Type measureType = updateRequest.getMeasure();
		if (weight != null && measureType == null) {
			result.reject("medicine.measure.not_null", "Укажите измерения для веса лекарства");
		}

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred whole updating medicine. The data is invalid. Medicine: %s. Id: %s. Result: %s",
					result, updateRequest, id, result);
		}

		LOG.debug("Checking medicine for existence by id '{}'", id);
		Medicine medicine = findById(id);

		if (weight != null && !weight.equals(medicine.getWeight())) {
			LOG.debug("Recognizing convert type for weight: {}", updateRequest);
			ConvertType convertType = ParamsChecker.checkConvertType(weight, convertTypeService, result);
			medicine.setConvertType(convertType);
		} else if (weight == null) {
			medicine.setWeight(null);
			medicine.setMeasure(null);
			medicine.setConvertType(null);
		}

		Measure measure = medicine.getMeasure();
		if (weight != null && (measure == null || !measure.getType().equals(measureType))) {
			LOG.debug("Checking measure for existence: {}", updateRequest);
			measure = measureService.findByType(measureType);
			medicine.setMeasure(measure);
		}

		LOG.debug("The data is OK. Medicine: {}. Id: {}", updateRequest, id);

		medicine = repository.save(medicine);
		LOG.info("The medicine was updated successfully: {}", medicine);
	}

	@Override
	public void deleteById(Long id) throws ValidationException, MedicineNotFoundException {
		LOG.debug("Checking id for not null");
		if (id == null) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "medicine");
			result.reject("medicine.delete.id.not_null", "Укажите идентификатор лекарства для удаления");
			throw new ValidationException("Exception occurred while deleting medicine by id. The id is null", result);
		}

		LOG.debug("Checking medicine for existence by id '{}'", id);
		Medicine medicine = findById(id);

		repository.delete(medicine);
		LOG.info("The medicine was deleted successfully by id '{}'", id);
	}

}
