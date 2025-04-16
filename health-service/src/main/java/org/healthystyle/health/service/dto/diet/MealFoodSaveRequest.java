package org.healthystyle.health.service.dto.diet;

import java.util.Objects;

import org.healthystyle.health.model.measure.Type;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MealFoodSaveRequest {
	@NotNull(message = "Укажите еду")
	private Long foodId;
	// food (grams int), liquid (litres float), count int
	// positive restriction
	@NotNull(message = "Укажите вес еды")
	@Positive(message = "Вес должен быть положительным числом")
	private Float weight;
//	@NotNull(message = "Укажите в чём измеряется вес еды")
//	private Type measureType;

	public Long getFoodId() {
		return foodId;
	}

	public void setFoodId(Long foodId) {
		this.foodId = foodId;
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

	@Override
	public int hashCode() {
		return Objects.hash(foodId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MealFoodSaveRequest other = (MealFoodSaveRequest) obj;
		return Objects.equals(foodId, other.foodId);
	}

}
