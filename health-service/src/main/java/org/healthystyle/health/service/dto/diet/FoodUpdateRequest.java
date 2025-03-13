package org.healthystyle.health.service.dto.diet;

import jakarta.validation.constraints.NotNull;

public class FoodUpdateRequest {
	@NotNull(message = "Введите название еды")
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
