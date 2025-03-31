package org.healthystyle.health.service.dto.sport;

import jakarta.validation.constraints.NotBlank;

public class ExerciseUpdateRequest {
	@NotBlank(message = "Укажите название упражнения")
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
