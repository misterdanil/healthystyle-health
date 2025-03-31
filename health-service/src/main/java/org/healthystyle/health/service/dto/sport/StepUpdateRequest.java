package org.healthystyle.health.service.dto.sport;

import jakarta.validation.constraints.NotBlank;

public class StepUpdateRequest {
	@NotBlank(message = "Укажите описание шага")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
