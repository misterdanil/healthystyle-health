package org.healthystyle.health.service.diet.impl;

import static java.util.Map.entry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.model.diet.FoodValue;
import org.healthystyle.health.repository.diet.FoodRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.diet.FoodService;
import org.healthystyle.health.service.diet.FoodValueService;
import org.healthystyle.health.service.dto.diet.FoodSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodUpdateRequest;
import org.healthystyle.health.service.dto.diet.FoodValueSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeMismatchException;
import org.healthystyle.health.service.error.diet.FoodExistException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodValueExistException;
import org.healthystyle.health.service.error.diet.NutritionValueNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.healthystyle.health.service.log.LogTemplate;
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
public class FoodServiceImpl implements FoodService {
	@Autowired
	private FoodRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private FoodValueService foodValueService;

	private static final Integer MAX_SIZE = 25;

	private static final String[] findParamNames = MethodNameHelper.getMethodParamNames(FoodService.class, "find",
			int.class, int.class);
	private static final String[] findByTitleParamNames = MethodNameHelper.getMethodParamNames(FoodService.class,
			"findByTitle", String.class, int.class, int.class);

	private static final Logger LOG = LoggerFactory.getLogger(FoodServiceImpl.class);

	@Override
	public Food findById(Long id) throws ValidationException, FoodNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "food");

		LOG.debug("Checking id for not null");
		if (id == null) {
			result.reject("food.find.id.not_null", "Укажите идентификатор для поиска");
			throw new ValidationException("Exception occurred while fetching a food by id. The id is null", result);
		}

		LOG.debug("Checking food by id '{}' for existence", id);
		Optional<Food> food = repository.findById(id);
		if (food.isEmpty()) {
			result.reject("food.find.not_found", "Не удалось найти еду по заданному идентификатору");
			throw new FoodNotFoundException(result, id);
		}
		LOG.info("Got food by id '{}' successfully");

		return food.get();
	}

	@Override
	public Page<Food> find(int page, int limit) throws ValidationException {
		String params = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry(findParamNames[0], page), entry(findParamNames[1], limit)));

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "food");
		LOG.debug("Validating params: {}", params);

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching foods. The params are invalid: %s. Result: %s", result, params,
					result);
		}

		LOG.debug("The params are valid: {}", params);

		LOG.debug("Getting health for fetching by params '{}'", params);
		Health health = healthAccessor.getHealth();

		Page<Food> foods = repository.find(health.getId(), PageRequest.of(page, limit));
		LOG.info("Got foods by params '{}' successfully", params);

		return foods;
	}

	@Override
	public Page<Food> findByTitle(String title, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(findByTitleParamNames, title, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "food");
		LOG.debug("Validating params: {}", params);

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (title == null) {
			result.reject("food.find.title.not_null", "Укажите название еды для поиска");
		}

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching foods by title. The params are invalid: %s. Result: %s", result,
					params, result);
		}

		LOG.debug("The param are valid: {}", params);

		LOG.debug("Getting heath for fetching by title: {}", params);
		Health health = healthAccessor.getHealth();

		Page<Food> foods = repository.findByTitle(title, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got foods by params '{}' successfully", params);

		return foods;
	}

	@Override
	public Food save(FoodSaveRequest saveRequest)
			throws ValidationException, FoodExistException, NutritionValueNotFoundException, FoodValueExistException,
			FoodNotFoundException, ConvertTypeMismatchException {
		LOG.debug("Validating the data: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "food");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("Exception occurred while saving food. The data is invalid: %s. Result: %s",
					result, saveRequest, result);
		}

		LOG.debug("Getting health to save food: {}", saveRequest);
		Health health = healthAccessor.getHealth();

		String title = saveRequest.getTitle();
		LOG.debug("Checking title for existence: {}", saveRequest);
		if (repository.existsByTitle(title, health.getId())) {
			result.reject("food.save.title.exists", "Еда с таким названием уже существует");
			throw new FoodExistException(title, result);
		}

		LOG.debug("Saving food: {}", saveRequest);
		Food food = new Food(title, health);
		food = repository.save(food);

		List<FoodValueSaveRequest> foodValues = saveRequest.getFoodValues();
		if (foodValues != null && !foodValues.isEmpty()) {
			LOG.debug("Saving food values: {}", saveRequest);
			for (FoodValueSaveRequest foodValue : foodValues) {
				LOG.debug("Saving food value: {}", foodValue);
				FoodValue savedFoodValue = foodValueService.save(foodValue, food.getId());
				LOG.debug("Adding food value '{}' to food '{}'", savedFoodValue, food);
				food.addFoodValue(savedFoodValue);
			}
		}

		LOG.info("Food was saved successfully: {}", food);

		return food;
	}

	@Override
	public void update(FoodUpdateRequest updateRequest, Long foodId)
			throws ValidationException, FoodNotFoundException, FoodExistException {
		LOG.debug("Validating food '{}' and food id '{}'", updateRequest, foodId);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "food");
		validator.validate(updateRequest, result);
		if (foodId == null) {
			result.reject("food.update.food_id.not_null", "Введите еду, для которой вы хотите изменить данные");
		}
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while updating food. The data is invalid. Food: %s. Food id: %s. Result: %s",
					result, updateRequest, foodId, result);
		}

		LOG.debug("Checking food for existence by id '{}'", foodId);
		Food food = findById(foodId);

		LOG.debug("Getting health to update food. Food: {}. Food id: {}", updateRequest, foodId);
		Health health = healthAccessor.getHealth();

		String title = updateRequest.getTitle();
		if (!title.equals(food.getTitle())) {
			LOG.debug("Checking new title for existence. Food: {}. Food id: {}", updateRequest, foodId);
			if (repository.existsByTitle(title, health.getId())) {
				result.reject("food.update.title.exists", "Еда с таким названием уже существует");
				throw new FoodExistException(title, result);
			}
			food.setTitle(title);
		}

		LOG.debug("The data is OK. Food: {}. Food id: {}", food, foodId);

		repository.save(food);
		LOG.info("The food was updated successfully: {}", food);
	}

	@Override
	public void deleteById(Long id) throws FoodNotFoundException, ValidationException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "food");

		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("food.delete.id.not_null", "Укажите идентификатор для удаления еды");
			throw new ValidationException("Exception occurred while deleting food by id. The id is null", result);
		}

		LOG.debug("Checking food for existence by id '{}'", id);
		if (!repository.existsById(id)) {
			result.reject("food.delete.id.not_exists");
			throw new FoodNotFoundException(result, id);
		}

		LOG.debug("The id is OK: {}", id);

		repository.deleteById(id);
		LOG.info("The food was deleted successfully by id: {}", id);
	}

}
