package org.healthystyle.health.service.diet.impl;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.diet.FoodValue;
import org.healthystyle.health.repository.diet.FoodValueRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.diet.FoodValueService;
import org.healthystyle.health.service.diet.NutritionValueService;
import org.healthystyle.health.service.dto.diet.FoodValueSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodValueUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.validation.ParamsChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
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
	private HealthAccessor healthAccessor;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(FoodValueServiceImpl.class);

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
	public Page<FoodValue> findByValueAndNutritionValue(String value, Long nutritionValueId, int page, int limit) {
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
	}

	@Override
	public Integer findSumByFoodsAndNutritionValue(List<Long> foodIds, Long nutritionValueId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<FoodValue> findAvgRangeWeek(Long nutritionValueId, Long healthId, Instant start, Instant to, int page,
			int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<FoodValue> findAvgRangeMonth(Long nutritionValueId, Long healthId, Instant start, Instant to, int page,
			int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<FoodValue> findAvgRangeYear(Long nutritionValueId, Long healthId, Instant start, Instant to, int page,
			int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FoodValue save(FoodValueSaveRequest saveRequest, Long mealId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(FoodValueUpdateRequest updateRequest, Long foodValueId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByIds(List<Long> ids) {
		// TODO Auto-generated method stub

	}

}
