package org.healthystyle.health.service.diet.impl;

import static java.util.Map.entry;

import java.time.Instant;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.health.model.diet.Diet;
import org.healthystyle.health.model.diet.Meal;
import org.healthystyle.health.repository.diet.MealRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.diet.DietService;
import org.healthystyle.health.service.diet.MealFoodService;
import org.healthystyle.health.service.diet.MealFoodService.MealFoodNotFoundException;
import org.healthystyle.health.service.diet.MealService;
import org.healthystyle.health.service.dto.diet.MealFoodSaveRequest;
import org.healthystyle.health.service.dto.diet.MealFoodUpdateRequest;
import org.healthystyle.health.service.dto.diet.MealReplaceRequest;
import org.healthystyle.health.service.dto.diet.MealSaveRequest;
import org.healthystyle.health.service.dto.diet.MealUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.diet.DietNotFoundException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodSetNotFoundException;
import org.healthystyle.health.service.error.diet.MealExistsException;
import org.healthystyle.health.service.error.diet.MealFoodExistException;
import org.healthystyle.health.service.error.diet.MealNotFoundException;
import org.healthystyle.health.service.error.diet.NoFoodsException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
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
public class MealServiceImpl implements MealService {
	@Autowired
	private MealRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private DietService dietService;
	@Autowired
	private MealFoodService mealFoodService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(MealServiceImpl.class);

	@Override
	public Meal findById(Long id) throws MealNotFoundException {
		LOG.debug("Getting a meal by id: {}", id);
		Optional<Meal> meal = repository.findById(id);
		if (meal.isEmpty()) {
			BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "meal");
			result.reject("meal.find.id.not_exists", "Блюдо с данным идентификатором не найдено");
			throw new MealNotFoundException(id, result);
		}

		LOG.info("Got the meal by id: {}", id);
		return meal.get();
	}

	private Meal findByDietAndDayAndTime(Long dietId, Integer day, LocalTime time) {
		String params = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry("dietId", dietId), entry("day", day), entry("time", time)));

		LOG.debug("Getting a meal by params: {}", params);
		Meal meal = repository.findByDietAndDayAndTime(dietId, day, time);
		if (meal == null) {
			LOG.debug("The meal by params was not found: {}", params);
			return null;
		}

		LOG.info("Got the meal by params: {}", params);
		return meal;
	}

	@Override
	public Page<Meal> findByFoods(List<Long> foodIds, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(
				Map.ofEntries(entry("foodIds", foodIds), entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "meal");
		if (foodIds == null || foodIds.isEmpty()) {
			result.reject("meal.find.food_ids.notEmpty", "Укажите блюда для поиска");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching meal by foods. The params are invalid. The params: %s. Result: %s",
					result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Page<Meal> meals = repository.findByFoods(foodIds, healthId, PageRequest.of(page, limit));
		LOG.info("Got meals by params {} successfully", params);

		return meals;
	}

	@Override
	public List<Meal> findByDate(Instant date, int page, int limit) throws ValidationException {
		String params = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry("date", date), entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "meal");
		if (date == null) {
			result.reject("meal.find.date.notNull", "Укажите дату для поиска");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching meals by date. The params are invalid. The params: %s. Result: %s",
					result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		List<Meal> meals = repository.findByDate(date, healthId, page, limit);
		LOG.info("Got meals by params {} successfully", params);

		return meals;
	}

	@Override
	public Page<Meal> findByDay(Integer day, int page, int limit) throws ValidationException {
		String params = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry("day", day), entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "meal");
		if (day == null) {
			result.reject("meal.find.day.notNull", "Укажите день недели для поиска");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching meals by day. The params are invalid. The params: %s. Result: %s",
					result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Page<Meal> meals = repository.findByDay(day, healthId, PageRequest.of(page, limit));
		LOG.info("Got meals by params {} successfully", params);

		return meals;
	}

	@Override
	public List<Meal> findPlanned(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(Map.ofEntries(entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "meal");

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching planned meals. The params are invalid. The params: %s. Result: %s",
					result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		List<Meal> meals = repository.findPlanned(healthId, page, limit);
		LOG.info("Got meals by params {} successfully", params);

		return meals;
	}

	@Override
	public List<Meal> findNextMeal(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(Map.ofEntries(entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "meal");

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching next meals. The params are invalid. The params: %s. Result: %s",
					result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		List<Meal> meals = repository.findNextMeal(healthId, page, limit);
		LOG.info("Got meals by params {} successfully", params);

		return meals;
	}

	@Override
	public Page<Meal> findByDiet(Long dietId, int page, int limit) throws ValidationException {
		String params = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry("dietId", dietId), entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "meal");
		if (dietId == null) {
			result.reject("meal.find.diet_id.notNull", "Укажите диету для поиска");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching meals by diet. The params are invalid. The params: %s. Result: %s",
					result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Page<Meal> meals = repository.findByDiet(dietId, healthId, PageRequest.of(page, limit));
		LOG.info("Got meals by params {} successfully", params);

		return meals;
	}

	@Override
	public Meal save(MealSaveRequest saveRequest, Long dietId)
			throws ValidationException, MealExistsException, DietNotFoundException, FoodNotFoundException,
			MealNotFoundException, MealFoodExistException, MeasureNotFoundException, ConvertTypeNotRecognizedException {
		LOG.debug("Validating a meal: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "meal");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while saving a meal. The meal is invalid. The meal: %s. The result: %s", result,
					saveRequest, result);
		}

		Diet diet;
		try {
			diet = dietService.findById(dietId);
		} catch (DietNotFoundException e) {
			result.reject("meal.save.diet_id.not_exists", "Диета не найдена для добавления в неё блюда");
			throw e;
		}

		Integer day = saveRequest.getDay();
		LocalTime time = saveRequest.getTime();
		LOG.debug("Checking meal for existence by day {} and time {}. Meal: {}", day, time, saveRequest);
		Meal existingMeal = findByDietAndDayAndTime(dietId, day, time);
		if (existingMeal != null) {
			result.reject("meal.save.day_time.duplicate", "На это время уже указан приём пищи");
			throw new MealExistsException(result, dietId, day, time, existingMeal);
		}
		LOG.debug("The meal by diet id {}, day {} and time {} was not found");

		Meal meal = new Meal(time, day, diet);
		List<MealFoodSaveRequest> mealFoods = saveRequest.getMealFoods();
		LOG.debug("Saving meal foods. The meal: {}", saveRequest);
		for (MealFoodSaveRequest mealFood : mealFoods) {
			mealFoodService.save(mealFood);
		}

		LOG.debug("The meal '{}' is valid. Saving: {}", saveRequest, meal);
		meal = repository.save(meal);
		LOG.info("The meal '{}' was saved successfully", meal);

		return meal;

	}

	@Override
	public boolean existsByDietAndDayAndTime(Long dietId, Integer day, LocalTime time) {
		String params = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry("dietId", dietId), entry("day", day), entry("time", time)));

		LOG.debug("Checking meal for existence by params: {}", params);
		boolean result = repository.existsByDietAndDayAndTime(dietId, day, time);
		LOG.debug("Got result by params {}: {}", params, result);

		return result;
	}

	@Override
	public boolean existsById(Long mealId) throws ValidationException {
		LOG.debug("Checking meal id for not null");
		if (mealId == null) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "meal");
			result.reject("meal.exists.id.not_null");
			throw new ValidationException("Exception occurred while checking for existing a meal by id. The id is null",
					result);
		}

		boolean exists = repository.existsById(mealId);
		LOG.info("Got exists result by id {}: {}", mealId, exists);

		return exists;
	}

	@Override
	public void update(MealUpdateRequest updateRequest, Long mealId) throws ValidationException, MealNotFoundException,
			NoFoodsException, FoodNotFoundException, MealFoodExistException, MeasureNotFoundException,
			ConvertTypeNotRecognizedException, MealFoodNotFoundException, MealExistsException {
		LOG.debug("Validating a meal {}: {}", mealId, updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "meal");
		if (mealId == null) {
			result.reject("meal.update.id.not_null", "Укажите идентификатор блюда для обновления");
			throw new ValidationException("Exception occurred while updating a meal '%s'. Meal id is null", result,
					updateRequest);
		}

		Meal meal = findById(mealId);

		Set<MealFoodSaveRequest> mealFoods = updateRequest.getMealFoods();
		if (mealFoods != null && !mealFoods.isEmpty()) {
			LOG.debug("Saving meal foods '{}' for meal '{}'", mealFoods, mealId);
			for (MealFoodSaveRequest mealFood : mealFoods) {
				LOG.debug("Saving meal food '{}' for meal '{}'", mealFood, mealId);
				mealFoodService.save(mealFood);
			}
		}

		Set<MealFoodUpdateRequest> updateMealFoods = updateRequest.getUpdateMealFoods();
		if (updateMealFoods != null && !updateMealFoods.isEmpty()) {
			LOG.debug("Updating meal foods '{}' for meal '{}'", updateMealFoods, mealId);
			for (MealFoodUpdateRequest mealFood : updateMealFoods) {
				LOG.debug("Updating meal food '{}' for meal '{}'", mealFood, mealId);
				mealFoodService.update(mealFood, mealId);
			}
		}

		Set<Long> removeMealFoodIds = updateRequest.getRemoveMealFoodIds();
		if (removeMealFoodIds != null && !removeMealFoodIds.isEmpty()) {
			LOG.debug("Deleting meal foods '{}' from meal '{}'", removeMealFoodIds, mealId);
			mealFoodService.deleteByIds(removeMealFoodIds, meal.getId());
		}

		Long dietId = meal.getDiet().getId();

		Integer day = updateRequest.getDay();
		LocalTime time = updateRequest.getTime();
		// time format to delete seconds and remain only hours and minutes
		if (meal.getDay() != day || !meal.getTime().equals(time)) {
			LOG.debug("Checking time and day for meal '{}':", mealId, updateRequest);
			if (repository.existsByDietAndDayAndTime(dietId, day, time)) {
				result.reject("meal.update.day_time.exists", "На это время и день уже запланирован приём пищи");
				throw new MealExistsException(result, dietId, day, time, meal);
			}
			meal.setDay(day);
			meal.setTime(time);
		}

		LOG.debug("The update data is OK. Updating the meal '{}': {}", mealId, updateRequest);
		repository.save(meal);
		LOG.info("The meal '{}' has been updated successfully: {}", mealId, updateRequest);
	}

	@Override
	public Meal replace(MealReplaceRequest replaceRequest) throws ValidationException, MealExistsException,
			DietNotFoundException, FoodNotFoundException, FoodSetNotFoundException, MealNotFoundException,
			MealFoodExistException, MeasureNotFoundException, ConvertTypeNotRecognizedException {
		LOG.debug("Validating the meal: {}", replaceRequest);
		BindingResult result = new BeanPropertyBindingResult(replaceRequest, "meal");
		validator.validate(replaceRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while replacing a meal. The data is invalid: %s. Result: %s", result,
					replaceRequest, result);
		}

		Long mealId = replaceRequest.getMealId();
		LOG.debug("Checking meal id '{}' for existence", mealId);
		Meal replacedMeal = findById(mealId);

		LOG.debug("The data is valid: {}", replaceRequest);

		LOG.debug("Deleting replaced meal: {}", replacedMeal);
		repository.delete(replacedMeal);

		MealSaveRequest newMeal = new MealSaveRequest(replaceRequest.getMealFoods(), replacedMeal.getTime(),
				replacedMeal.getDay());
		Meal meal = save(newMeal, replacedMeal.getDiet().getId());
		LOG.info("The meal was replaced successfully from '{}' to '{}'", replacedMeal, meal);

		return meal;
	}

	@Override
	public void deleteByIds(Set<Long> ids) throws ValidationException {
		LOG.debug("Checking meal id for not null");
		if (ids == null) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "meal");
			result.reject("meal.delete.ids.not_null");
			throw new ValidationException("Exception occurred while deleting meals by ids. The ids is null", result);
		}

		repository.deleteByIds(ids);
		LOG.info("Meals was deleted successfully by ids '{}'", ids);
	}

}
