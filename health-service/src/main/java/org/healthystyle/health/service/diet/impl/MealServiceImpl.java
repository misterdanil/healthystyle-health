package org.healthystyle.health.service.diet.impl;

import static java.util.Map.entry;

import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.health.model.diet.Diet;
import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.model.diet.FoodSet;
import org.healthystyle.health.model.diet.Meal;
import org.healthystyle.health.repository.diet.MealRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.diet.DietService;
import org.healthystyle.health.service.diet.FoodService;
import org.healthystyle.health.service.diet.FoodSetService;
import org.healthystyle.health.service.diet.MealService;
import org.healthystyle.health.service.dto.diet.MealSaveRequest;
import org.healthystyle.health.service.dto.diet.MealUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.DietNotFoundException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodSetNotFoundException;
import org.healthystyle.health.service.error.diet.MealExistsException;
import org.healthystyle.health.service.error.diet.MealNotFoundException;
import org.healthystyle.health.service.error.diet.NoFoodsException;
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
	private FoodService foodService;
	@Autowired
	private FoodSetService foodSetService;

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
	public Page<Meal> findByFoodSet(Long foodSetId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(
				Map.ofEntries(entry("foodSetId", foodSetId), entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "meal");
		if (foodSetId == null) {
			result.reject("meal.find.food_set.notNull", "Укажите набор блюд для поиска");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching meal by food set. The params are invalid. The params: %s. Result: %s",
					result, params, result);
		}

		Long healthId = healthAccessor.getHealth().getId();

		Page<Meal> meals = repository.findByFoodSet(foodSetId, healthId, PageRequest.of(page, limit));
		LOG.info("Got meals by params {} successfully", params);

		return meals;
	}

	@Override
	public Page<Meal> findByDate(Instant date, int page, int limit) throws ValidationException {
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

		Page<Meal> meals = repository.findByDate(date, healthId, PageRequest.of(page, limit));
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
	public Page<Meal> findPlanned(int page, int limit) throws ValidationException {
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

		Page<Meal> meals = repository.findPlanned(healthId, PageRequest.of(page, limit));
		LOG.info("Got meals by params {} successfully", params);

		return meals;
	}

	@Override
	public Page<Meal> findNextMeal(int page, int limit) throws ValidationException {
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

		Page<Meal> meals = repository.findNextMeal(healthId, page, limit);
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
	public Meal save(MealSaveRequest saveRequest, Long dietId) throws ValidationException, MealExistsException,
			DietNotFoundException, FoodNotFoundException, FoodSetNotFoundException {
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

		Meal meal;
		List<Long> foodIds = saveRequest.getFoodIds();
		Food[] foods = new Food[foodIds.size()];
		FoodSet foodSet = null;
		if (foodIds != null && !foodIds.isEmpty()) {
			LOG.debug("Checking foods for existence: {}. Meal: {}", foodIds, saveRequest);
			List<Long> notFoundIds = new ArrayList<>();
			for (int i = 0; i < foodIds.size(); i++) {
				Long foodId = foodIds.get(i);
				LOG.debug("Checking food for existence: {}. Meal: {}", foodId, saveRequest);
				try {
					Food food = foodService.findById(foodId);
					foods[i] = food;
				} catch (FoodNotFoundException e) {
					notFoundIds.add(foodId);
				}
			}

			if (notFoundIds.size() > 0) {
				throw new FoodNotFoundException(result, notFoundIds.toArray(new Long[notFoundIds.size()]));
			}

			LOG.debug("All foods was found. Meal: {}", saveRequest);
			meal = new Meal(time, day, diet, foods);
		} else {
			Long foodSetId = saveRequest.getFoodSetId();

			LOG.debug("Checking food set for existence: {}. Meal: {}", foodSetId, saveRequest);
			foodSet = foodSetService.findById(foodSetId);
			LOG.debug("Food set was found. Meal: {}", saveRequest);

			meal = new Meal(time, day, diet, foodSet);
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
	public void update(MealUpdateRequest updateRequest, Long mealId)
			throws ValidationException, MealNotFoundException, NoFoodsException {
		LOG.debug("Validating a meal {}: {}", mealId, updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "meal");
		if (mealId == null) {
			result.reject("meal.update.id.not_null", "Укажите идентификатор блюда для обновления");
			throw new ValidationException("Exception occurred while updating a meal '%s'. Meal id is null", result,
					updateRequest);
		}

		Set<Long> foodIds = updateRequest.getFoodIds();
		Long foodSetId = updateRequest.getFoodSetId();
		if ((foodIds != null && foodIds.size() > 0) && foodSetId != null
				|| ((foodIds == null) || foodIds.isEmpty()) && foodSetId == null) {
			result.reject("meal.update.food_and_set.not_null",
					"Укажите либо еду, либо набор еды, но не то и не другое вместе");
			throw new ValidationException(
					"Exception occurred while updating a meal '%s'. Foods '%s' and food set id '%s' are pointed both",
					result, mealId, foodIds, foodSetId);
		}

		Meal meal = repository.findById(mealId).get();
		if (meal == null) {
			result.reject("meal.update.not_exist", "Блюда с данным идентификатором не существует");
			throw new MealNotFoundException(mealId, result);
		}

		Set<Long> removeFoodIds = updateRequest.getRemoveFoodIds();
		if (removeFoodIds != null) {
			LOG.debug("Deleting foods '{}' from meal '{}'", removeFoodIds, mealId);
			repository.deleteFoodsById(mealId, removeFoodIds);
			LOG.info("Deleted all foods '{}' from meal '{}'", removeFoodIds, mealId);
		}

		if (foodIds != null && !foodIds.isEmpty()) {
			LOG.debug("Getting foods by ids {} for meal '{}'", foodIds, mealId);
			List<Food> foods = foodService.findByIdsExcludeMeal(foodIds, mealId);
			if (foods.isEmpty()) {
				result.reject("meal.update.food_ids.not_found",
						"По данным идентификаторам блюд не удалось найти ни одной еды");
				throw new FoodNotFoundException(result, foodIds.toArray(new Long[foodIds.size()]));
			}
			meal.setFoodSet(null);
			repository.addFoods(foods, mealId);
			if (repository.countFoods(mealId) == 0) {
				result.reject("meal.update.no_foods", "Укажите хотя бы одну еду");
				throw new NoFoodsException(mealId, result);
			}
			LOG.info("Added new foods {} to meal: {}", foods, mealId);
		} else if (foodSetId != null) {
			LOG.debug("Getting food set for meal: {}", mealId);
			FoodSet foodSet = foodSetService.findById(foodSetId);
			meal.clearFoods();
			meal.setFoodSet(foodSet);
			LOG.info("Set food set to '{}' for meal '{}'", foodSet, mealId);
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
}
