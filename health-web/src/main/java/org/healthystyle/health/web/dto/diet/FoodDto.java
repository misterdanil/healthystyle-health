package org.healthystyle.health.web.dto.diet;

import java.time.Instant;
import java.util.List;

public class FoodDto {
	private Long id;
	private String title;
	private List<FoodValueDto> foodValues;
	private Instant createdOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<FoodValueDto> getFoodValues() {
		return foodValues;
	}

	public void setFoodValues(List<FoodValueDto> foodValues) {
		this.foodValues = foodValues;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Instant createdOn) {
		this.createdOn = createdOn;
	}

}
