package org.healthystyle.health.service.medicine.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class TreatmentSaveRequest {
	@NotBlank(message = "Укажите описание лечения")
	private String description;
	@NotEmpty(message = "Укажите планы")
	private List<PlanSaveRequest> plans;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<PlanSaveRequest> getPlans() {
		return plans;
	}

	public void setPlans(List<PlanSaveRequest> plans) {
		this.plans = plans;
	}

}
