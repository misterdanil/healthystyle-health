package org.healthystyle.health.service.dto.diet;

import org.healthystyle.health.model.measure.Type;

public class MealFoodUpdateRequest {
	// food (grams int), liquid (litres float), count int
	// positive restriction
	private String weight;
	private Type measureType;

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Type getMeasureType() {
		return measureType;
	}

	public void setMeasureType(Type measureType) {
		this.measureType = measureType;
	}

}
