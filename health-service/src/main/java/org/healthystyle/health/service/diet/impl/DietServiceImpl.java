package org.healthystyle.health.service.diet.impl;

import static java.util.Map.entry;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.diet.Diet;
import org.healthystyle.health.model.diet.Meal;
import org.healthystyle.health.repository.diet.DietRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.diet.DietService;
import org.healthystyle.health.service.diet.MealService;
import org.healthystyle.health.service.dto.diet.DietSaveRequest;
import org.healthystyle.health.service.dto.diet.DietUpdateRequest;
import org.healthystyle.health.service.dto.diet.MealSaveRequest;
import org.healthystyle.health.service.dto.diet.MealUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.diet.DietExistException;
import org.healthystyle.health.service.error.diet.DietNotFoundException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodSetNotFoundException;
import org.healthystyle.health.service.error.diet.MealExistsException;
import org.healthystyle.health.service.error.diet.MealFoodExistException;
import org.healthystyle.health.service.error.diet.MealFoodNotFoundException;
import org.healthystyle.health.service.error.diet.MealNotFoundException;
import org.healthystyle.health.service.error.diet.MealSaveException;
import org.healthystyle.health.service.error.diet.MealTimeDuplicateException;
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

import jakarta.transaction.Transactional;

@Service
public class DietServiceImpl implements DietService {
	@Autowired
	private DietRepository repository;
	@Autowired
	private MealService mealService;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private Validator validator;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(DietServiceImpl.class);

	public Diet findById(Long id) throws ValidationException, DietNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "diet");

		LOG.debug("Checking id for not null");
		if (id == null) {
			result.reject("diet.find.id.not_null", "Укажите идентификатор диеты для поиска");
			throw new ValidationException("Exception occurred while fetching a diet by id. The id is null", result);
		}

		Optional<Diet> diet = repository.findById(id);
		if (diet.isEmpty()) {
			result.reject("diet.find.not_found", "Не удалось найти диету по заданному идентификатору");
			throw new DietNotFoundException(id, result);
		}
		LOG.info("Got diet by id '{}' successfully", id);

		return diet.get();
	}

	@Override
	public Page<Diet> findByTitle(String title, int page, int limit) throws ValidationException {
		String paramsTemplate = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry("title", title), entry("page", page), entry("limit", limit)));
		LOG.debug("Validating params: {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "diet");

		LOG.debug("Checking title for not null");
		if (title == null) {
			result.reject("diet.find.title.not_null", "Укажите название для поиска");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching diets. The params are not valid. Params: %s. Result %s.", result,
					paramsTemplate, result);
		}

		LOG.debug("The params are valid: {}", paramsTemplate);

		Health health = healthAccessor.getHealth();
		Page<Diet> diets = repository.findByTitle(title, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got diets by params {} successfully", paramsTemplate);

		return diets;
	}

	@Override
	public Page<Diet> findByStart(Instant start, int page, int limit) throws ValidationException {
		String paramsTemplate = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry("start", start), entry("page", page), entry("limit", limit)));
		LOG.debug("Validating params: {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "diet");

		LOG.debug("Checking start for not null");
		if (start == null) {
			result.reject("diet.find.start.not_null", "Укажите дату начала для поиска");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching diets. The params are not valid. Params: %s. Result %s.", result,
					paramsTemplate, result);
		}

		LOG.debug("The params are valid: {}", paramsTemplate);

		Health health = healthAccessor.getHealth();
		Page<Diet> diets = repository.findByStart(start, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got diets by params {} successfully", paramsTemplate);

		return diets;
	}

	@Override
	public Page<Diet> findActual(int page, int limit) throws ValidationException {
		String paramsTemplate = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry("page", page), entry("limit", limit)));
		LOG.debug("Validating params: {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "diet");

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching diets. The params are not valid. Params: %s. Result %s.", result,
					paramsTemplate, result);
		}

		LOG.debug("The params are valid: {}", paramsTemplate);

		LOG.debug("Getting health for fetch by params {}", paramsTemplate);
		Health health = healthAccessor.getHealth();

		Page<Diet> diets = repository.findActual(health.getId(), PageRequest.of(page, limit));
		LOG.info("Got diets by params {} successfully", paramsTemplate);

		return diets;
	}

	@Override
	public Page<Diet> findByDate(Instant date, int page, int limit) throws ValidationException {
		String paramsTemplate = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry("date", date), entry("page", page), entry("limit", limit)));
		LOG.debug("Validating params: {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "diet");

		LOG.debug("Checking date for not null");
		if (date == null) {
			result.reject("diet.find.date.not_null", "Укажите дату для поиска диет");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching diets. The params are not valid. Params: %s. Result %s.", result,
					paramsTemplate, result);
		}

		LOG.debug("The params are valid: {}", paramsTemplate);

		Health health = healthAccessor.getHealth();
		Page<Diet> diets = repository.findByDate(date, health.getId(), PageRequest.of(page, limit));
		LOG.info("Got diets by params {} successfully", paramsTemplate);

		return diets;
	}

	@Override
	@Transactional
	public Diet save(DietSaveRequest saveRequest)
			throws ValidationException, DietExistException, MealTimeDuplicateException, MealSaveException {
		LOG.debug("Validating a diet: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "diet");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("Exception occurred while saving a diet. The diet is invalid. Result: %s",
					result, result);
		}

		LOG.debug("Getting health for diet: {}", saveRequest);
		Health health = healthAccessor.getHealth();

		String title = saveRequest.getTitle();
		LOG.debug("Checking diet for existence by title: {}", saveRequest);
		if (repository.existsByTitle(title, health.getId())) {
			result.reject("diet.save.title.exists");
			throw new DietExistException(title, result);
		}

		List<MealSaveRequest> meals = saveRequest.getMeals();
		LOG.debug("Validating meals: {}. Diet: {}", meals, saveRequest);
		Set<MealSaveRequest> duplicates = new HashSet<>();
		Set<MealSaveRequest> uniqueMeals = new HashSet<>();
		meals.forEach(meal -> {
			if (!uniqueMeals.contains(meal)) {
				LOG.debug("Found meal: {}. Meals: {}", meal, meals);
				uniqueMeals.add(meal);
			} else {
				LOG.warn("Found duplicate: {}. Meals: {}", meal, meals);
				duplicates.add(meal);
			}
		});

		if (!duplicates.isEmpty()) {
			throw new MealTimeDuplicateException(duplicates, result);
		}

		Diet diet = new Diet(saveRequest.getTitle(), saveRequest.getStart(), saveRequest.getEnd(), health);
		diet = repository.save(diet);

		LOG.debug("Saving meals: {}. The diet: {}", meals, saveRequest);
		for (MealSaveRequest mealSaveRequest : meals) {
			try {
				Meal meal;
				try {
					meal = mealService.save(mealSaveRequest, diet.getId());
					LOG.debug("Meal '{}' was saved successfully. Adding to diet: {}", meal, diet.getId());
					diet.addMeal(meal);
				} catch (ValidationException | FoodNotFoundException | MealNotFoundException | MealFoodExistException
						| MeasureNotFoundException | ConvertTypeNotRecognizedException e) {
					throw new MealSaveException(mealSaveRequest, diet.getId(), e);
				}
			} catch (MealExistsException e) {
				throw new RuntimeException(
						"Exception occurred while saving a diet. Meal exists but diet is not stil saved", e);
			} catch (DietNotFoundException e) {
				throw new RuntimeException(
						"Exception occurred while saving a diet. The diet was saved during transaction, but the meal couldn't find it",
						e);
			}
		}

		return diet;
	}

	@Override
	public void update(DietUpdateRequest updateRequest, Long dietId)
			throws ValidationException, DietNotFoundException, DietExistException {
		LOG.debug("Validating data: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "diet");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while updating a diet '%s'. The data is invalid: %s. Result: %s", result,
					dietId, updateRequest, result);
		}

		LOG.debug("Getting health for update diet: {}", updateRequest);
		Health health = healthAccessor.getHealth();

		LOG.debug("Checking diet for existence by id '{}'", dietId);
		Diet diet = findById(dietId);

		String title = updateRequest.getTitle();
		if (!title.equals(diet.getTitle())) {
			LOG.debug("Checking diet for existence by title: ", updateRequest);
			if (repository.existsByTitle(title, health.getId())) {
				result.reject("diet.update.title.exists", "Диета с данным названием уже существует");
				throw new DietExistException(title, result);
			}
		}

		Instant start = updateRequest.getStart();
		if (!start.equals(diet.getStart())) {
			diet.setStart(start);
		}

		Instant end = updateRequest.getEnd();
		if (!end.equals(diet.getEnd())) {
			diet.setEnd(end);
		}

		LOG.debug("The data is valid: {}", updateRequest);

		repository.save(diet);
		LOG.info("The diet was update successfully: '{}'", diet);
	}

	@Override
	public void deleteById(Long id) throws ValidationException, DietNotFoundException {
		LOG.debug("Validating an id: {}", id);
		if (id == null) {
			BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "diet");
			result.reject("diet.delete.id.not_null", "Укажите идентификатор диеты для удаления");
			throw new ValidationException("Exception occurred while deleteing a diet by id. The id is null. Result: {}",
					result, result);
		}

		LOG.debug("Getting a diet by id: {}", id);
		Diet diet = findById(id);

		repository.delete(diet);
		LOG.info("The diet '{}' was deleted successfully", id);
	}

}
