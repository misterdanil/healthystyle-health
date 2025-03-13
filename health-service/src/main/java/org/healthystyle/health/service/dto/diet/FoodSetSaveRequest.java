package org.healthystyle.health.service.dto.diet;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

public class FoodSetSaveRequest {
	@NotEmpty(message = "Укажите название для набора еды")
	private String title;
	@NotEmpty(message = "Укажите идентификаторы еды для добавления в набор")
	private Set<Long> foodIds;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Long> getFoodIds() {
		return foodIds;
	}

	public void setFoodIds(Set<Long> foodIds) {
		this.foodIds = foodIds;
	}

}
