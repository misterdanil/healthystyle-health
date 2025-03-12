package org.healthystyle.health.service.diet.impl;

import static java.util.Map.entry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.model.diet.Meal;
import org.healthystyle.health.model.diet.MealFood;
import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.repository.diet.MealFoodRepository;
import org.healthystyle.health.service.diet.FoodService;
import org.healthystyle.health.service.diet.MealFoodService;
import org.healthystyle.health.service.diet.MealService;
import org.healthystyle.health.service.dto.diet.MealFoodSaveRequest;
import org.healthystyle.health.service.dto.diet.MealFoodUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.MealFoodExistException;
import org.healthystyle.health.service.error.diet.MealNotFoundException;
import org.healthystyle.health.service.error.diet.NoFoodsException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.measure.convert.ConvertTypeNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.measure.MeasureService;
import org.healthystyle.health.service.measure.convert.ConvertTypeService;
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
public class MealFoodServiceImpl implements MealFoodService {
	@Autowired
	private MealFoodRepository repository;
	@Autowired
	private MealService mealService;
	@Autowired
	private FoodService foodService;
	@Autowired
	private MeasureService measureService;
	@Autowired
	private ConvertTypeService convertTypeService;
	@Autowired
	private Validator validator;
	private static final String[] FIND_BY_MEAL_PARAMS = MethodNameHelper.getMethodParamNames(MealFoodService.class,
			"findByMeal", Long.class, int.class, int.class);

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(MealFoodServiceImpl.class);

	@Override
	public MealFood findById(Long id) throws ValidationException, MealFoodNotFoundException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "mealFood");

		LOG.debug("Checking id for not null");
		if (id == null) {
			result.reject("meal.find.id.not_null", "Укажите идентификатор для поиска");
			throw new ValidationException("Exception occurred while fetching meal food by id. The id is null", result);
		}

		LOG.debug("Getting a meal by id '{}'", id);
		Optional<MealFood> mealFood = repository.findById(id);
		if (mealFood.isEmpty()) {
			result.reject("meal.find.not_found");
			throw new MealFoodNotFoundException(id, result);
		}
		LOG.info("Got meal by id '{}' successfully");

		return mealFood.get();
	}

	@Override
	public Page<MealFood> findByMeal(Long mealId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(Map.ofEntries(entry(FIND_BY_MEAL_PARAMS[0], mealId),
				entry(FIND_BY_MEAL_PARAMS[1], page), entry(FIND_BY_MEAL_PARAMS[2], limit)));

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "mealFood");
		if (mealId == null) {
			result.reject("meal_food.find.meal_id.not_null", "Укажите идентификатор блюда для поиска");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching meal foods. The params are not valid. The params: %s. Result: %s",
					result, params, result);
		}

		LOG.debug("The params are valid");

		Page<MealFood> mealFoods = repository.findByMeal(mealId, PageRequest.of(page, limit));
		LOG.info("Got meal foods successfully by params: {}", params);

		return mealFoods;
	}

	@Override
	public MealFood save(MealFoodSaveRequest saveRequest) throws ValidationException, MealNotFoundException,
			MealFoodExistException, FoodNotFoundException, MeasureNotFoundException, ConvertTypeNotRecognizedException {
		LOG.debug("Validating params: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "mealFood");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while saving a meal food. The data is invalid. Result: %s", result, result);
		}

		Long mealId = saveRequest.getMealId();
		LOG.debug("Checking meal id '{}' for existing", mealId);
		Meal meal = mealService.findById(mealId);

		Long foodId = saveRequest.getFoodId();
		LOG.debug("Checking food id '{}' for existing", foodId);
		Food food = foodService.findById(foodId);

		if (repository.existsByMealAndFood(mealId, foodId)) {
			result.reject("meal_food.save.exists", "Еда уже была добавлена в данный приём пищи");
			throw new MealFoodExistException(mealId, foodId, result);
		}

		Type measureType = saveRequest.getMeasureType();
		LOG.debug("Checking measure type '{}' for existing", measureType);
		Measure measure = measureService.findByType(measureType);

		String weight = saveRequest.getWeight();
		LOG.debug("Checking weight '{}' for possible convert type", weight);
		ConvertType convertType = convertTypeService.getConvertTypeByValue(weight);

		LOG.debug("The data is valid: {}", saveRequest);

		MealFood mealFood = new MealFood(meal, food, weight, measure, convertType);
		mealFood = repository.save(mealFood);
		LOG.info("The meal food '{}' was saved successfully", mealFood);

		return mealFood;
	}

	@Override
	public void update(MealFoodUpdateRequest updateRequest, Long id) throws ValidationException,
			MeasureNotFoundException, ConvertTypeNotRecognizedException, MealFoodNotFoundException {
		LOG.debug("Validating the data: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "mealFood");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while updating a meal food. The params is invalid. Result: %s", result, result);
		}

		LOG.debug("Getting a meal food by id '{}' for update '{}'", id, updateRequest);
		MealFood mealFood = findById(id);

		Type measureType = updateRequest.getMeasureType();
		if (measureType != null) {
			LOG.debug("Checking measure type '{}' for existing", measureType);
			Measure measure = measureService.findByType(measureType);
			mealFood.setMeasure(measure);
		}

		String weight = updateRequest.getWeight();
		if (weight != null) {
			LOG.debug("Checking weight '{}' for possible convert type", weight);
			ConvertType convertType = convertTypeService.getConvertTypeByValue(weight);
			mealFood.setConvertType(convertType);
		}

		LOG.debug("The data '{}' and meal id '{}' are OK", updateRequest, id);

		repository.save(mealFood);
		LOG.info("Meal food was updated successfully: {}", mealFood);
	}

	@Override
	public void deleteById(Long id) throws ValidationException, MealFoodNotFoundException {
		LOG.debug("Checking id for not null");
		if (id == null) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "mealFood");
			result.reject("meal_food.delete.id.not_null", "Укажите идентификатор для удаления");
			throw new ValidationException("Exception occurred while deleting a meal food by id. The id is null",
					result);
		}

		LOG.debug("Checking meal food by id '{}' for existence", id);
		MealFood mealFood = findById(id);

		repository.delete(mealFood);
		LOG.info("A meal food '{}' was deleted successfully", mealFood);
	}

	@Override
	public void deleteByIds(Set<Long> ids, Long mealId) throws ValidationException, NoFoodsException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "mealFood");

		LOG.debug("Checking ids '{}' for emptiness");
		if (ids == null || ids.isEmpty()) {
			result.reject("meal_food.delete.ids.not_empty", "Укажите хотя один идентификатор для удаления");
			throw new ValidationException(
					"Exception occurred while deleting meal foods by ids. The ids is null or empty", result);
		}

		LOG.debug("Deleting meal foods by ids: {}", ids);
		repository.deleteByIds(ids);

		LOG.debug("Checking count foods for not 0");
		if (repository.countFoods(mealId) == 0) {
			result.reject("meal.update.no_foods", "В приёме пищи не осталось ни одной еды. Добавьте хотя бы одну");
			throw new NoFoodsException(mealId, result);
		}

		LOG.info("Meal foods was deleted successfully by ids '{}'", ids);
	}

}
