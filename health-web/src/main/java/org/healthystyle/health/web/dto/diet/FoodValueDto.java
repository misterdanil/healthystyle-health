package org.healthystyle.health.web.dto.diet;

public class FoodValueDto {
	private Long id;
	private String value;
	private NutritionValueDto nutritionValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public NutritionValueDto getNutritionValue() {
		return nutritionValue;
	}

	public void setNutritionValue(NutritionValueDto nutritionValue) {
		this.nutritionValue = nutritionValue;
	}

}
