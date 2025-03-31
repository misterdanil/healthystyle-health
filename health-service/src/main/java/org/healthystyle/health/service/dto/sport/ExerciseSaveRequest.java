package org.healthystyle.health.service.dto.sport;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class ExerciseSaveRequest {
	@NotBlank(message = "Укажите название упражнения")
	private String title;
	@NotEmpty(message = "Укажите шаги упражнения")
	private List<StepSaveRequest> steps;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<StepSaveRequest> getSteps() {
		return steps;
	}

	public void setSteps(List<StepSaveRequest> steps) {
		this.steps = steps;
	}

}
