package org.healthystyle.health.service.diet;

import java.time.Instant;
import java.util.List;

import org.healthystyle.health.model.diet.FoodValue;
import org.healthystyle.health.service.dto.diet.FoodValueSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodValueUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
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

	Page<FoodValue> findByFood(Long foodId, int page, int limit) throws ValidationException;

	Page<FoodValue> findByValueAndNutritionValue(String value, Long nutritionValueId, int page, int limit);

	Integer findSumByFoodsAndNutritionValue(List<Long> foodIds, Long nutritionValueId);

	Page<FoodValue> findAvgRangeWeek(Long nutritionValueId, Long healthId, Instant start, Instant to, int page,
			int limit);

	Page<FoodValue> findAvgRangeMonth(Long nutritionValueId, Long healthId, Instant start, Instant to, int page,
			int limit);

	Page<FoodValue> findAvgRangeYear(Long nutritionValueId, Long healthId, Instant start, Instant to, int page,
			int limit);

	FoodValue save(FoodValueSaveRequest saveRequest, Long mealId);

	void update(FoodValueUpdateRequest updateRequest, Long foodValueId);

	void deleteByIds(List<Long> ids);
}
