package org.healthystyle.health.service.dto.diet;

import jakarta.validation.constraints.NotNull;

public class FoodValueSaveRequest {
	@NotNull(message = "Введите значение для пищевой ценности")
	private String value;
	@NotNull(message = "Укажите пищевую ценность")
	private Long nutritionValueId;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getNutritionValueId() {
		return nutritionValueId;
	}

	public void setNutritionValueId(Long nutritionValueId) {
		this.nutritionValueId = nutritionValueId;
	}

}
