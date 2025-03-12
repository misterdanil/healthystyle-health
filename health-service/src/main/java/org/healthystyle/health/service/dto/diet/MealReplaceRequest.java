package org.healthystyle.health.service.dto.diet;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MealReplaceRequest {
	@NotNull(message = "Укажите идентификатор заменяемого блюда")
	private Long mealId;
	@NotEmpty(message = "Укажите еду для добавления")
	private List<MealFoodSaveRequest> mealFoods;

	public Long getMealId() {
		return mealId;
	}

	public void setMealId(Long mealId) {
		this.mealId = mealId;
	}

	public List<MealFoodSaveRequest> getMealFoods() {
		return mealFoods;
	}

	public void setMealFoods(List<MealFoodSaveRequest> mealFoods) {
		this.mealFoods = mealFoods;
	}

}
