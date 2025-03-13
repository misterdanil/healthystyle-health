package org.healthystyle.health.service.dto.diet;

import org.healthystyle.health.model.diet.Value;

import jakarta.validation.constraints.NotNull;

public class FoodValueSaveRequest {
	@NotNull(message = "Введите значение для пищевой ценности")
	private String value;
	@NotNull(message = "Укажите пищевую ценность")
	private Value nutritionValue;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Value getNutritionValue() {
		return nutritionValue;
	}

	public void setNutritionValue(Value nutritionValue) {
		this.nutritionValue = nutritionValue;
	}

}
