package org.healthystyle.health.service.dto.diet;

import java.time.LocalTime;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

public class MealUpdateRequest {
	private Set<Long> foodIds;
	private Long foodSetId;
	private Set<Long> removeFoodIds;
	@NotNull(message = "Укажите время приёма пищи")
	private LocalTime time;
	@NotNull(message = "Укажите день приёма пищи")
	private Integer day;

	public Set<Long> getFoodIds() {
		return foodIds;
	}

	public void setFoodIds(Set<Long> foodIds) {
		this.foodIds = foodIds;
	}

	public Long getFoodSetId() {
		return foodSetId;
	}

	public void setFoodSetId(Long foodSetId) {
		this.foodSetId = foodSetId;
	}

	public Set<Long> getRemoveFoodIds() {
		return removeFoodIds;
	}

	public void setRemoveFoodIds(Set<Long> removeFoodIds) {
		this.removeFoodIds = removeFoodIds;
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
