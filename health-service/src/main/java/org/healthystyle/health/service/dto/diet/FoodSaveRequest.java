package org.healthystyle.health.service.dto.diet;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class FoodSaveRequest {
	@NotEmpty(message = "Укажите название еды")
	private String title;
	private List<FoodValueSaveRequest> foodValues;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<FoodValueSaveRequest> getFoodValues() {
		return foodValues;
	}

	public void setFoodValues(List<FoodValueSaveRequest> foodValues) {
		this.foodValues = foodValues;
	}

}
