package org.healthystyle.health.service.medicine.dto;

import org.healthystyle.health.model.measure.Type;

import jakarta.validation.constraints.NotBlank;

public class MedicineUpdateRequest {
	@NotBlank(message = "Укажите название лекарства")
	private String name;
	private String weight;
	private Type measure;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Type getMeasure() {
		return measure;
	}

	public void setMeasure(Type measure) {
		this.measure = measure;
	}

}
