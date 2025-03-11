package org.healthystyle.health.service.dto.diet;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class MealReplaceRequest {
	@NotNull(message = "Укажите идентификатор заменяемого блюда")
	private Long mealId;
	@Valid
	@NotNull(message = "Укажите блюдо для замены")
	private MealSaveRequest replaceMeal;

	public Long getMealId() {
		return mealId;
	}

	public void setMealId(Long mealId) {
		this.mealId = mealId;
	}

	public MealSaveRequest getReplaceMeal() {
		return replaceMeal;
	}

	public void setReplaceMeal(MealSaveRequest replaceMeal) {
		this.replaceMeal = replaceMeal;
	}

}
