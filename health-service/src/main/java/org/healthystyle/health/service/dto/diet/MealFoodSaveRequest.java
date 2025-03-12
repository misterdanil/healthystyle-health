package org.healthystyle.health.service.dto.diet;

import org.healthystyle.health.model.measure.Type;

import jakarta.validation.constraints.NotNull;

public class MealFoodSaveRequest {
	@NotNull(message = "Укажите блюдо")
	private Long mealId;
	@NotNull(message = "Укажите еду")
	private Long foodId;
	// food (grams int), liquid (litres float), count int
	// positive restriction
	@NotNull(message = "Укажите вес еды")
	private String weight;
	@NotNull(message = "Укажите в чём измеряется вес еды")
	private Type measureType;

	public Long getMealId() {
		return mealId;
	}

	public void setMealId(Long mealId) {
		this.mealId = mealId;
	}

	public Long getFoodId() {
		return foodId;
	}

	public void setFoodId(Long foodId) {
		this.foodId = foodId;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Type getMeasureType() {
		return measureType;
	}

	public void setMeasureType(Type measureType) {
		this.measureType = measureType;
	}

}
