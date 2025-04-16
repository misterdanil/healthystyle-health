package org.healthystyle.health.service.diet.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.model.diet.FoodValue;
import org.healthystyle.health.model.diet.NutritionValue;
import org.healthystyle.health.model.diet.Value;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.repository.diet.FoodValueRepository;
import org.healthystyle.health.repository.result.AvgStatistic;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.diet.FoodService;
import org.healthystyle.health.service.diet.FoodValueService;
import org.healthystyle.health.service.diet.NutritionValueService;
import org.healthystyle.health.service.dto.diet.FoodValueSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodValueUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeMismatchException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodValueExistException;
import org.healthystyle.health.service.error.diet.FoodValueNotFoundException;
import org.healthystyle.health.service.error.diet.NutritionValueNotFoundException;
import org.healthystyle.health.service.log.LogTemplate;
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
public class FoodValueServiceImpl implements FoodValueService {
	@Autowired
	private FoodValueRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private NutritionValueService nutritionValueService;
	@Autowired
	private FoodService foodService;
	@Autowired
	private HealthAccessor healthAccessor;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(FoodValueServiceImpl.class);

	@Override
	public FoodValue findById(Long id) throws ValidationException, FoodValueNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodValue");

		LOG.debug("Checking id for not null");
		if (id == null) {
			result.reject("food_value.find.id_not_null", "Укажите идентификатор для поиска пищевой ценности");
			throw new ValidationException("Exception occurred while fetching food value by id. The id is null", result);
		}

		Optional<FoodValue> foodValue = repository.findById(id);
		if (foodValue.isEmpty()) {
			result.reject("food_value.find.not_found", "Не удалось найти пищевую ценность");
			throw new FoodValueNotFoundException(id, result);
		}
		LOG.info("Got food value by id '{}' successfully", id);

		return foodValue.get();
	}

	@Override
	public Page<FoodValue> findByFood(Long foodId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_FOOD_PARAM_NAMES, foodId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodValue");
		LOG.debug("Validating params: {}", params);
		if (foodId == null) {
			result.reject("food_value.find.food_id.not_null", "Укажите еду для поиска");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching food values by food id. The params are invalid: %s. Result: %s",
					result, params, result);
		}

		LOG.debug("Getting health to fetch by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<FoodValue> foodValues = repository.findByFood(foodId, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got food values by params '{}' successfully", params);

		return foodValues;
	}

	@Override
	public Page<FoodValue> findByValueAndNutritionValue(String value, Long nutritionValueId, int page, int limit)
			throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_VALUE_AND_NUTRITION_VALUE_PARAM_NAMES, value,
				nutritionValueId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodValue");
		LOG.debug("Validating params: {}", params);
		if (value == null || value.isEmpty()) {
			result.reject("food_value.find.value.not_empty", "Укажите значения для поиска относительно его");
		}
		if (nutritionValueId == null) {
			result.reject("food_value.find.nutrition_value_id.not_null",
					"Укажите идентификатор пищевой ценности для поиска");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching food values. The params are invalid: %s. Result: %s", result,
					params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<FoodValue> foodValues = repository.findByValueAndNutritionValue(value, nutritionValueId,
				PageRequest.of(page, limit));
		LOG.info("Got food value by params '{}' successfully", params);

		return foodValues;
	}

	@Override
	public Integer findSumByFoodsAndNutritionValue(List<Long> foodIds, Long nutritionValueId)
			throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_SUM_BY_FOODS_AND_NUTRITION_VALUE, foodIds, nutritionValueId);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodValue");
		LOG.debug("Validating params: {}", params);
		if (foodIds == null || foodIds.isEmpty()) {
			result.reject("food_value.find.food_ids.not_empty", "Укажите еду для суммирования пищевой ценности");
		}
		if (nutritionValueId == null) {
			result.reject("food_value.find.nutrition_value_id.not_null",
					"Укажите пищевую ценность для суммирования показателей");
		}
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching sum of nutrition value of food values. The params are invalid: %s. Result: %s ",
					result, params, result);
		}

		LOG.debug("The params are valid: {}", params);

		Integer sum = repository.findSumByFoodsAndNutritionValue(foodIds, nutritionValueId);
		LOG.info("Got sum of nutrition value '{}' of foods '{}' successfully", nutritionValueId, foodIds);

		return sum;
	}

	@Override
	public Page<AvgStatistic> findAvgRangeWeek(Long nutritionValueId, LocalDate start, LocalDate end, int page,
			int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_AVG_RANGE_WEEK_PARAM_NAMES, nutritionValueId, start, end,
				page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodValue");
		LOG.debug("Validating params: {}", params);
		if (nutritionValueId == null) {
			result.reject("food_value.find.nutrition_value_id.not_null", "Укажите пищевую ценность");
		}
		if (start == null) {
			result.reject("food_value.find.start.not_null", "Укажите дату начала поиска");
		}
		if (end == null) {
			result.reject("food_value.find.nutrition_value_id.not_null", "Укажите дату конца поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching avg range week food values. The params are invalid: %s. Result: %s",
					result, params, result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get average range weeks food values: {}", params);
		Health health = healthAccessor.getHealth();

		Page<AvgStatistic> foodValues = repository
				.findAvgRangeWeek(nutritionValueId, health.getId(), start, end, PageRequest.of(page, limit))
				.map(fv -> new AvgStatistic(((Timestamp) fv[0]).toLocalDateTime(), (String) fv[1]));
		LOG.info("Got food values by params '{}' successfully", params);

		return foodValues;
	}

	@Override
	public Page<AvgStatistic> findAvgRangeMonth(Long nutritionValueId, LocalDate start, LocalDate end, int page,
			int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_AVG_RANGE_MONTH_PARAM_NAMES, nutritionValueId, start, end,
				page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodValue");
		LOG.debug("Validating params: {}", params);
		if (nutritionValueId == null) {
			result.reject("food_value.find.nutrition_value_id.not_null", "Укажите пищевую ценность");
		}
		if (start == null) {
			result.reject("food_value.find.start.not_null", "Укажите дату начала поиска");
		}
		if (end == null) {
			result.reject("food_value.find.nutrition_value_id.not_null", "Укажите дату конца поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching avg range month food values. The params are invalid: %s. Result: %s",
					result, params, result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get average range month food values: {}", params);
		Health health = healthAccessor.getHealth();

		Page<AvgStatistic> foodValues = repository
				.findAvgRangeMonth(nutritionValueId, health.getId(), start, end, PageRequest.of(page, limit))
				.map(fv -> new AvgStatistic(((Timestamp) fv[0]).toLocalDateTime(), (String) fv[1]));
		LOG.info("Got food values by params '{}' successfully", params);

		return foodValues;
	}

	@Override
	public Page<AvgStatistic> findAvgRangeYear(Long nutritionValueId, LocalDate start, LocalDate end, int page,
			int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_AVG_RANGE_YEAR_PARAM_NAMES, nutritionValueId, start, end,
				page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodValue");
		LOG.debug("Validating params: {}", params);
		if (nutritionValueId == null) {
			result.reject("food_value.find.nutrition_value_id.not_null", "Укажите пищевую ценность");
		}
		if (start == null) {
			result.reject("food_value.find.start.not_null", "Укажите дату начала поиска");
		}
		if (end == null) {
			result.reject("food_value.find.nutrition_value_id.not_null", "Укажите дату конца поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching avg range year food values. The params are invalid: %s. Result: %s",
					result, params, result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health to get average range year food values: {}", params);
		Health health = healthAccessor.getHealth();

		Page<AvgStatistic> foodValues = repository.findAvgRangeYear(nutritionValueId, health.getId(), start, end,
				PageRequest.of(page, limit))
				.map(fv -> new AvgStatistic(((Timestamp) fv[0]).toLocalDateTime(), (String) fv[1]));
		LOG.info("Got food values by params '{}' successfully", params);

		return foodValues;
	}

	@Override
	@Transactional
	public FoodValue save(FoodValueSaveRequest saveRequest, Long foodId)
			throws ValidationException, NutritionValueNotFoundException, FoodValueExistException, FoodNotFoundException,
			ConvertTypeMismatchException {
		LOG.debug("Validating food value '{}' and food id '{}'", saveRequest, foodId);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "foodValue");
		validator.validate(saveRequest, result);
		if (foodId == null) {
			result.reject("food_value.save.food_id.not_null", "Укажите еду для добавления пищевой ценности");
		}
		LOG.debug("Checking value for positive: {}", saveRequest);
		String value = saveRequest.getValue();
		if (Float.valueOf(value) < 0) {
			result.reject("food_value.save.value.negative", "Значение должно быть положительным");
		}
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while saving a food value. The data is invalid. Food value: %s. Food id: %s, Result: %s",
					result, saveRequest, foodId, result);
		}

		LOG.debug("Checking food '{}' for existence", foodId);
		Food food = foodService.findById(foodId);

		Value nutrition = saveRequest.getNutritionValue();
		LOG.debug("Checking nutrition value for existence: {}", saveRequest);
		NutritionValue nutritionValue = nutritionValueService.findByValue(nutrition);

		LOG.debug("Checking food value for existence: {}", saveRequest);
		if (repository.existsByFoodAndNutritionValue(foodId, nutrition)) {
			result.reject("food_value.save.exists", "Пищевая ценность для данной еды уже существует");
			throw new FoodValueExistException(foodId, nutrition, result);
		}

		ConvertType convertType = nutritionValue.getConvertType();
		LOG.debug("Checking value for convert type '{}': {}", convertType, saveRequest);
		if (!convertType.support(value)) {
			result.reject("food_value.save.value.mismatch", "Значение не соответствует типу пищевой ценности");
			throw new ConvertTypeMismatchException(convertType.getId(), value, result);
		}

		LOG.debug("The food value '{}' and food id '{}' are OK: {}", saveRequest, foodId);

		FoodValue foodValue = new FoodValue(value, nutritionValue, food);
		repository.save(foodValue);
		LOG.info("Food value was saved successfully: {}", foodValue);

		return foodValue;
	}

	@Override
	public void update(FoodValueUpdateRequest updateRequest, Long foodValueId)
			throws ValidationException, FoodValueNotFoundException, ConvertTypeMismatchException {
		LOG.debug("Validating update data '{}' and food value id '{}'", updateRequest, foodValueId);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "foodValue");
		validator.validate(updateRequest, result);
		if (foodValueId == null) {
			result.reject("food_value.update.id.not_null", "Укажите пищевую ценность для её обновления");
		}
		LOG.debug("Checking value for positive: {}", updateRequest);
		String value = updateRequest.getValue();
		if (Float.valueOf(value) < 0) {
			result.reject("food_value.update.value.negative", "Значение должно быть положительным");
		}
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while updating food value'. The data is invalid. Update data: %s. Food value id: %s. Result: %s",
					result, updateRequest, foodValueId, result);
		}

		LOG.debug("Checking food value for existence by id '{}'", foodValueId);
		FoodValue foodValue = findById(foodValueId);
		NutritionValue nutritionValue = foodValue.getNutritionValue();

		ConvertType convertType = nutritionValue.getConvertType();
		LOG.debug("Checking value for convert type '{}': {}", convertType, updateRequest);
		if (!convertType.support(value)) {
			result.reject("food_value.update.value.mismatch", "Значение не соответствует типу пищевой ценности");
			throw new ConvertTypeMismatchException(convertType.getId(), value, result);
		}

		LOG.debug("The food value '{}' and id '{}' are OK", updateRequest, foodValueId);

		foodValue.setValue(value);
		repository.save(foodValue);
	}

	@Override
	public void deleteByIds(Set<Long> ids) throws ValidationException {
		LOG.debug("Checking ids for emptiness: {}", ids);
		if (ids == null || ids.isEmpty()) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "foodValue");
			result.reject("food_value.delete.ids.not_empty", "Укажите идентификаторы для удаления");
			throw new ValidationException("The ids is null or empty", result);
		}

		repository.deleteByIds(ids);
		LOG.info("Food values was deleted successfully by ids: {}", ids);
	}

}
