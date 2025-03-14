package org.healthystyle.health.service.medicine.dto;

import jakarta.validation.constraints.NotBlank;

public class TreatmentSaveRequest {
	@NotBlank(message = "Укажите описание лечения")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
