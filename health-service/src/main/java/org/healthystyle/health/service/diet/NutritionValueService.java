package org.healthystyle.health.service.diet;

import org.healthystyle.health.model.diet.NutritionValue;
import org.healthystyle.health.model.diet.Value;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.NutritionValueNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface NutritionValueService {
	static final String[] FIND_PARAM_NAMES = MethodNameHelper.getMethodParamNames(NutritionValueService.class, "find",
			int.class, int.class);

	static final String[] FIND_BY_VALUE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(NutritionValueService.class,
			"findByValue", String.class, int.class, int.class);

	NutritionValue findByValue(Value value) throws ValidationException, NutritionValueNotFoundException;

	Page<NutritionValue> find(int page, int limit) throws ValidationException;

	Page<NutritionValue> findByValue(String value, int page, int limit) throws ValidationException;

}
