package org.healthystyle.health.service.dto.diet;

import jakarta.validation.constraints.NotEmpty;

public class FoodValueUpdateRequest {
	@NotEmpty(message = "Укажите значение пищевой ценности")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
