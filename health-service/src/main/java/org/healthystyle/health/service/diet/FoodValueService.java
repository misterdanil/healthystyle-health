package org.healthystyle.health.service.diet;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.healthystyle.health.model.diet.FoodValue;
import org.healthystyle.health.service.dto.diet.FoodValueSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodValueUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeMismatchException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodValueExistException;
import org.healthystyle.health.service.error.diet.FoodValueNotFoundException;
import org.healthystyle.health.service.error.diet.NutritionValueNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface FoodValueService {
	static final String[] FIND_BY_FOOD_PARAM_NAMES = MethodNameHelper.getMethodParamNames(FoodValueService.class,
			"findByFood", Long.class, int.class, int.class);
	static final String[] FIND_BY_VALUE_AND_NUTRITION_VALUE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(
			FoodValueService.class, "findByValueAndNutritionValue", String.class, Long.class, int.class, int.class);
	static final String[] FIND_SUM_BY_FOODS_AND_NUTRITION_VALUE = MethodNameHelper
			.getMethodParamNames(FoodValueService.class, "findSumByFoodsAndNutritionValue", List.class, Long.class);
	static final String[] FIND_AVG_RANGE_WEEK_PARAM_NAMES = MethodNameHelper.getMethodParamNames(FoodValueService.class,
			"findAvgRangeWeek", Long.class, Long.class, Instant.class, Instant.class, int.class, int.class);
	static final String[] FIND_AVG_RANGE_MONTH_PARAM_NAMES = MethodNameHelper.getMethodParamNames(
			FoodValueService.class, "findAvgRangeMonth", Long.class, Long.class, Instant.class, Instant.class,
			int.class, int.class);
	static final String[] FIND_AVG_RANGE_YEAR_PARAM_NAMES = MethodNameHelper.getMethodParamNames(FoodValueService.class,
			"findAvgRangeYear", Long.class, Long.class, Instant.class, Instant.class, int.class, int.class);

	FoodValue findById(Long id) throws ValidationException, FoodValueNotFoundException;

	Page<FoodValue> findByFood(Long foodId, int page, int limit) throws ValidationException;

	Page<FoodValue> findByValueAndNutritionValue(String value, Long nutritionValueId, int page, int limit)
			throws ValidationException;

	Integer findSumByFoodsAndNutritionValue(List<Long> foodIds, Long nutritionValueId) throws ValidationException;

	Page<FoodValue> findAvgRangeWeek(Long nutritionValueId, Instant start, Instant to, int page, int limit)
			throws ValidationException;

	Page<FoodValue> findAvgRangeMonth(Long nutritionValueId, Instant start, Instant to, int page, int limit)
			throws ValidationException;

	Page<FoodValue> findAvgRangeYear(Long nutritionValueId, Instant start, Instant to, int page, int limit)
			throws ValidationException;

	FoodValue save(FoodValueSaveRequest saveRequest, Long foodId)
			throws ValidationException, NutritionValueNotFoundException, FoodValueExistException, FoodNotFoundException, ConvertTypeMismatchException;

	void update(FoodValueUpdateRequest updateRequest, Long foodValueId)
			throws ValidationException, FoodValueNotFoundException, ConvertTypeMismatchException;

	void deleteByIds(Set<Long> ids) throws ValidationException;
}
