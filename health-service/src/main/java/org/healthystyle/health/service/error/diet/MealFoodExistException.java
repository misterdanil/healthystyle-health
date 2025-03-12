package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class MealFoodExistException extends AbstractException {
	private Long mealId;
	private Long foodId;

	public MealFoodExistException(Long mealId, Long foodId, BindingResult result) {
		super(result, "A meal food with meal id '%s' and food id '%s' has already existed", mealId, foodId);
		this.mealId = mealId;
		this.foodId = foodId;
	}

	public Long getMealId() {
		return mealId;
	}

	public Long getFoodId() {
		return foodId;
	}

}
