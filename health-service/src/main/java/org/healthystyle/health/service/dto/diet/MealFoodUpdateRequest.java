package org.healthystyle.health.service.dto.diet;

import org.healthystyle.health.model.measure.Type;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MealFoodUpdateRequest {
	@NotNull(message = "Укажите идентификатор для обновления")
	private Long id;
	// food (grams int), liquid (litres float), count int
	// positive restriction
	@NotNull(message = "Укажите вес")
	@Positive(message = "Вес должен быть положительным")
	private Float weight;
//	private Type measureType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

//	public Type getMeasureType() {
//		return measureType;
//	}
//
//	public void setMeasureType(Type measureType) {
//		this.measureType = measureType;
//	}

}
