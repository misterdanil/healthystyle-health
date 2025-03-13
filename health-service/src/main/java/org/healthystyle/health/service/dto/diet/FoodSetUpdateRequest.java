package org.healthystyle.health.service.dto.diet;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

public class FoodSetUpdateRequest {
	@NotEmpty(message = "Укажите название для наборы еды")
	private String title;
	private Set<Long> removeFoodIds;
	private Set<Long> foodIds;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Long> getRemoveFoodIds() {
		return removeFoodIds;
	}

	public void setRemoveFoodIds(Set<Long> removeFoodIds) {
		this.removeFoodIds = removeFoodIds;
	}

	public Set<Long> getFoodIds() {
		return foodIds;
	}

	public void setFoodIds(Set<Long> foodIds) {
		this.foodIds = foodIds;
	}

}
