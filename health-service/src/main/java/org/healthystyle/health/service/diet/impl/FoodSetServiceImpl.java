package org.healthystyle.health.service.diet.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.model.diet.FoodSet;
import org.healthystyle.health.repository.diet.FoodSetRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.diet.FoodService;
import org.healthystyle.health.service.diet.FoodSetService;
import org.healthystyle.health.service.dto.diet.FoodSetSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodSetUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodSetExistException;
import org.healthystyle.health.service.error.diet.FoodSetNoFoodsException;
import org.healthystyle.health.service.error.diet.FoodSetNotFoundException;
import org.healthystyle.health.service.error.diet.NoFoodsException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.validation.ParamsChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

public class FoodSetServiceImpl implements FoodSetService {
	@Autowired
	private FoodSetRepository repository;
	@Autowired
	private FoodService foodService;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(FoodServiceImpl.class);

	@Override
	public FoodSet findById(Long id) throws ValidationException, FoodSetNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodSet");

		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("food_set.find.id.not_null", "Укажите идентификатор набора еды для поиска");
			throw new ValidationException("Exception occurred while fetching food set by id. The id is null", result);
		}

		Optional<FoodSet> foodSet = repository.findById(id);
		if (foodSet.isEmpty()) {
			result.reject("food_set.find.not_found", "Не удалось найти набор еды");
			throw new FoodSetNotFoundException(id, result);
		}
		LOG.info("Got food set by id '{}' successfully", id);

		return foodSet.get();
	}

	@Override
	public Page<FoodSet> findLast(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_LAST_PARAM_NAMES, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodSet");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching food sets. The params are invalid: %s. Result: %s", result,
					params, result);
		}

		LOG.debug("The params are OK: {}", params);

		LOG.debug("Getting health to get food sets by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<FoodSet> foodSets = repository.findLast(health.getId(), PageRequest.of(page, limit));
		LOG.info("Got food sets successfully by params: {}", params);

		return foodSets;
	}

	@Override
	public Page<FoodSet> findByTitle(String title, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_TITLE_PARAM_NAMES, title, page, limit);

		LOG.debug("Validating params: {}", params);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodSet");
		if (title == null) {
			result.reject("food_set.find.title.not_null", "Укажите название набора еды для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching food sets. The params are invalid: %s. Result: %s", result,
					params, result);
		}

		LOG.debug("Getting health to get food sets by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<FoodSet> foods = repository.findByTitle(title, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got food sets successfully by params: {}", params);

		return foods;
	}

	@Override
	public Page<FoodSet> findByFoods(List<Long> foodIds, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_FOODS_PARAM_NAMES, foodIds, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodSet");
		LOG.debug("Validating params: {}", params);
		if (foodIds == null || foodIds.isEmpty()) {
			result.reject("food_set.find.food_ids.not_empty", "Укажите еду для поиска наборов");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching food sets. The params are invalid: %s. Result: %s", result,
					params, result);
		}

		LOG.debug("The params are OK: {}", params);

		LOG.debug("Getting health to get food sets by params: {}", params);
		Health health = healthAccessor.getHealth();

		Page<FoodSet> foodSets = repository.findByFoods(foodIds, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got food sets successfully by params: {}", params);

		return foodSets;
	}

	@Override
	public FoodSet save(FoodSetSaveRequest saveRequest)
			throws ValidationException, FoodSetExistException, FoodNotFoundException {
		LOG.debug("Validating food set: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "foodSet");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while saving a food set. The params are invalid: %s. Result: %s", result,
					saveRequest, result);
		}

		LOG.debug("Getting health to save food set: {}", saveRequest);
		Health health = healthAccessor.getHealth();

		String title = saveRequest.getTitle();
		LOG.debug("Checking title for existence: {}", saveRequest);
		if (repository.existsByTitle(title, health.getId())) {
			result.reject("food_set.save.exists", "Набор еды с таким названием уже существует");
			throw new FoodSetExistException(title, result);
		}

		Set<Long> foodIds = saveRequest.getFoodIds();
		LOG.debug("Checking foods for existence: {}", saveRequest);
		List<Food> foundedFoods = foodService.findByIds(foodIds);
		List<Long> foundedFoodIds = foundedFoods.stream().map(f -> f.getId()).collect(Collectors.toList());
		if (foodIds.size() != foundedFoodIds.size()) {
			foodIds.removeAll(foundedFoodIds);
			result.reject("food_set.save.food_ids.not_found",
					"Не удалось найти следующую еду для добавления в набор: " + foodIds);
			throw new FoodNotFoundException(result, foodIds.toArray(new Long[foodIds.size()]));
		}

		LOG.debug("The params are valid: {}", saveRequest);

		FoodSet foodSet = new FoodSet(title, health, foundedFoods);
		foodSet = repository.save(foodSet);
		LOG.info("Food set was saved successfully: {}", foodSet);

		return foodSet;
	}

	protected Set<Long> getNotFoundedFoods(Set<Long> ids, List<Food> foods) {
		Set<Long> copyIds = new HashSet<Long>(ids);
		List<Long> foundedFoodIds = foods.stream().map(f -> f.getId()).collect(Collectors.toList());
		copyIds.removeAll(foundedFoodIds);

		return copyIds;
	}

	@Override
	public void update(FoodSetUpdateRequest updateRequest, Long foodSetId) throws ValidationException,
			FoodSetNotFoundException, FoodSetExistException, FoodNotFoundException, FoodSetNoFoodsException {
		LOG.debug("Validating food set '{}' and food set id '{}'", updateRequest, foodSetId);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "foodSet");
		validator.validate(updateRequest, result);
		if (foodSetId == null) {
			result.reject("food_set.update.food_set_id.not_null", "Укажите идентификатор набора еды для обновления");
		}
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while updating a food set. The data is invalid. Food set: %s. Food id: %s",
					result, updateRequest, foodSetId);
		}

		LOG.debug("Checking food set for existence by id '{}'", foodSetId);
		FoodSet foodSet = findById(foodSetId);

		LOG.debug("Getting health to update food set. Food set: %s. Food set id: %s", updateRequest, foodSetId);

		String title = updateRequest.getTitle();
		if (!title.equals(foodSet.getTitle())) {
			LOG.debug("Checking title for existence: {}", updateRequest);
			if (repository.existsByTitle(title, foodSetId)) {
				result.reject("food_set.update.title.exists", "Набор еды с таким названием уже существует");
				throw new FoodSetExistException(title, result);
			}

			foodSet.setTitle(title);
			repository.save(foodSet);
		}

		Set<Long> removeFoodIds = updateRequest.getRemoveFoodIds();
		if (removeFoodIds != null && !removeFoodIds.isEmpty()) {
			LOG.debug("Deleting foods '{}' from food set '{}'", removeFoodIds, foodSetId);
			repository.deleteFoodByIdsAndFoodSetId(removeFoodIds, foodSetId);
			LOG.debug("Food '{}' was deleted from food set '{}'", removeFoodIds, foodSetId);
		}

		Set<Long> foodIds = updateRequest.getFoodIds();
		if (foodIds != null && !foodIds.isEmpty()) {
			LOG.debug("Checking food ids for existence. Food set: {}. Food set id: {}", updateRequest, foodSetId);
			List<Food> foods = repository.findByFoodIdsExcludeExists(foodIds, foodSetId);
			Set<Long> notFoundedIds = getNotFoundedFoods(foodIds, foods);
			if (!notFoundedIds.isEmpty()) {
				result.reject("food_set.update.food_ids.not_found",
						"Не удалось найти следующую еду для формирования набора: " + notFoundedIds);
				throw new FoodNotFoundException(result, notFoundedIds.toArray(Long[]::new));
			}
			repository.addFoods(foods, foodSetId);
		}

		LOG.debug("Checking foods existence in food set '{}'", foodSetId);
		if (repository.countFoods(foodSetId) == 0) {
			result.reject("food_set.update.no_foods", "В наборе не осталось ни одной еды");
			throw new FoodSetNoFoodsException(foodSetId, result);
		}

	}

	@Override
	public void deleteById(Long id) throws ValidationException, FoodSetNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "foodSet");
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("food_set.delete.id.not_null");
			throw new ValidationException("Exception occurred while deleting food set by id. The id is null", result);
		}

		LOG.debug("Checking food set for existence by id '{}'", id);
		FoodSet foodSet = findById(id);

		repository.delete(foodSet);
		LOG.info("The food set was deleted successfully by id '{}'", id);
	}

}
