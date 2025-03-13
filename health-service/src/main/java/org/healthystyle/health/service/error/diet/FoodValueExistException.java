package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.model.diet.Value;
import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class FoodValueExistException extends AbstractException {
	private Long foodId;
	private Value nutritionValue;

	public FoodValueExistException(Long foodId, Value nutritionValue, BindingResult result) {
		super(result, "A food value for food '%s' and nutrition value '%s' has already existed", foodId, nutritionValue);
		this.foodId = foodId;
		this.nutritionValue = nutritionValue;
	}

	public Long getFoodId() {
		return foodId;
	}

	public Value getNutritionValue() {
		return nutritionValue;
	}

}
