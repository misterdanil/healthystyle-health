package org.healthystyle.health.service.diet.impl;

import java.util.LinkedHashMap;
import java.util.Optional;

import org.healthystyle.health.model.diet.NutritionValue;
import org.healthystyle.health.model.diet.Value;
import org.healthystyle.health.repository.diet.NutritionValueRepository;
import org.healthystyle.health.service.diet.NutritionValueService;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.NutritionValueNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@Service
public class NutritionValueServiceImpl implements NutritionValueService {
	@Autowired
	private NutritionValueRepository repository;

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

}
