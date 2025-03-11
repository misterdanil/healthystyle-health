package org.healthystyle.health.service.dto.diet;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class FoodSaveRequest {
	@NotEmpty(message = "Укажите название еды")
	private String title;
	// food (grams int), liquid (litres float), count int
	// positive restriction
	private String weight;
	private List<FoodValueSaveRequest> foodValues;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public List<FoodValueSaveRequest> getFoodValues() {
		return foodValues;
	}

	public void setFoodValues(List<FoodValueSaveRequest> foodValues) {
		this.foodValues = foodValues;
	}

}
