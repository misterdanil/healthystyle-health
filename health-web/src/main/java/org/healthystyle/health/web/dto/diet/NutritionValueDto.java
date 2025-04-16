package org.healthystyle.health.web.dto.diet;

import org.healthystyle.health.model.diet.Value;

public class NutritionValueDto {
	private Long id;
	private Value value;
	private String translation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

}
