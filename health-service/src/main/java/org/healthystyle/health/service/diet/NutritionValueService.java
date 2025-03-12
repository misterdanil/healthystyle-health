package org.healthystyle.health.service.diet;

import org.healthystyle.health.model.diet.NutritionValue;
import org.healthystyle.health.model.diet.Value;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.NutritionValueNotFoundException;

public interface NutritionValueService {
	NutritionValue findByValue(Value value) throws ValidationException, NutritionValueNotFoundException;
}
