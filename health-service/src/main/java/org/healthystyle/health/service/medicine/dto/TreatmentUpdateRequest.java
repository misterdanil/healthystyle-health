package org.healthystyle.health.service.medicine.dto;

import jakarta.validation.constraints.NotBlank;

public class TreatmentUpdateRequest {
	@NotBlank(message = "Укажите описание лечения для обновления")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
