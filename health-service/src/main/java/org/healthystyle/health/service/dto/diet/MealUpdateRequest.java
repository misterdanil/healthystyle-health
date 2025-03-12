package org.healthystyle.health.service.dto.diet;

import java.time.LocalTime;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

public class MealUpdateRequest {
	private Long id;
	private Set<MealFoodSaveRequest> mealFoods;
	private Set<MealFoodUpdateRequest> updateMealFoods;
	private Set<Long> removeMealFoodIds;
	@NotNull(message = "Укажите время приёма пищи")
	private LocalTime time;
	@NotNull(message = "Укажите день приёма пищи")
	private Integer day;

	public Long getId() {
		return id;
	}

	public Set<MealFoodSaveRequest> getMealFoods() {
		return mealFoods;
	}

	public void setMealFoods(Set<MealFoodSaveRequest> mealFoods) {
		this.mealFoods = mealFoods;
	}

	public Set<MealFoodUpdateRequest> getUpdateMealFoods() {
		return updateMealFoods;
	}

	public void setUpdateMealFoods(Set<MealFoodUpdateRequest> updateMealFoods) {
		this.updateMealFoods = updateMealFoods;
	}

	public Set<Long> getRemoveMealFoodIds() {
		return removeMealFoodIds;
	}

	public void setRemoveMealFoodIds(Set<Long> removeMealFoodIds) {
		this.removeMealFoodIds = removeMealFoodIds;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

}
