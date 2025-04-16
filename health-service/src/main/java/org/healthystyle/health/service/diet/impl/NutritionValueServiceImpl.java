package org.healthystyle.health.service.diet.impl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.healthystyle.health.model.diet.NutritionValue;
import org.healthystyle.health.model.diet.Value;
import org.healthystyle.health.repository.diet.NutritionValueRepository;
import org.healthystyle.health.service.diet.NutritionValueService;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.NutritionValueNotFoundException;
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

@Service
public class NutritionValueServiceImpl implements NutritionValueService {
	@Autowired
	private NutritionValueRepository repository;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(NutritionValueServiceImpl.class);

	@Override
	public NutritionValue findByValue(Value value) throws ValidationException, NutritionValueNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "nutritionValue");

		LOG.debug("Checking value for not null");
		if (value == null) {
			result.reject("nutrition_value.value.not_null", "Укажите пищевую ценность для поиска");
			throw new ValidationException(
					"Exception occurred while fetching nutrition value by value. The value is null", result);
		}

		NutritionValue nutritionValue = repository.findByValue(value);
		LOG.debug("Checking nutrition value for existence by value '{}'", value);
		if (nutritionValue == null) {
			result.reject("nutrition_value.find.not_found", "Не удалось найти пищевую ценность по данному значению");
			throw new NutritionValueNotFoundException(value, result);
		}

		LOG.info("Got nutrition value by value '{}' successfully", value);

		return nutritionValue;
	}

	@Override
	public Page<NutritionValue> find(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PARAM_NAMES, page, limit);

		LOG.debug("Validating param: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "nutritionValue");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The param are invalid: %s. Result: %s", result, params, result);
		}

		Page<NutritionValue> nutritionValues = repository.findAll(PageRequest.of(page, limit));

		LOG.info("Got nutrition values successfully by params '{}'", params);

		return nutritionValues;
	}

	@Override
	public Page<NutritionValue> findByValue(String value, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_VALUE_PARAM_NAMES, value, page, limit);

		LOG.debug("Validating param: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "nutritionValue");
		if (value == null) {
			result.reject("nutrition_value.find.value.not_null", "Укажите название для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The param are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("Getting values by string value: {}", value);
		List<Value> values = Arrays.stream(Value.values())
				.filter(v -> v.toString().toLowerCase().contains(value.toLowerCase())).toList();
		LOG.debug("Got values by string '{}': {}", value, values);

		Page<NutritionValue> nutritionValues;
		if (values == null || values.isEmpty()) {
			params = LogTemplate.getParamsTemplate(FIND_PARAM_NAMES, page, limit);

			LOG.debug("Nothing to search by value: {}. Getting values by page and limit only", value);
			nutritionValues = repository.findAll(PageRequest.of(page, limit));
			LOG.info("Got nutrtion values successfully by params: {}", params);

			return nutritionValues;
		} else {
			nutritionValues = repository.findByValues(values, PageRequest.of(page, limit));
		}
		LOG.info("Got nutrition values successfully by params '{}'", params);

		return nutritionValues;
	}

	public static void main(String[] args) {
		System.out.println(Arrays.stream(Value.values())
				.filter(v -> v.toString().toLowerCase().contains("Углеfgfdg".toLowerCase())).toList());
	}

}
