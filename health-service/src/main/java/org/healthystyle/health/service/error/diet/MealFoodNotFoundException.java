package org.healthystyle.health.service.error.diet;

import java.util.List;
import java.util.Map;

import org.healthystyle.health.service.dto.diet.MealSaveRequest;
import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class MealFoodNotFoundException extends AbstractException {
	private Map<MealSaveRequest, List<Long>> mealFoodIds;
	private Map<MealSaveRequest, Long> mealFoodSetIds;

	public MealFoodNotFoundException(BindingResult result, Map<MealSaveRequest, List<Long>> mealFoodIds,
			Map<MealSaveRequest, Long> mealFoodSetIds) {
		super(result, "Could not found foods and food sets for meal. Foods: %s. Food sets: %s", mealFoodIds,
				mealFoodSetIds);
		this.mealFoodIds = mealFoodIds;
		this.mealFoodSetIds = mealFoodSetIds;
	}

	public Map<MealSaveRequest, List<Long>> getMealFoodIds() {
		return mealFoodIds;
	}

	public Map<MealSaveRequest, Long> getMealFoodSetIds() {
		return mealFoodSetIds;
	}

}
