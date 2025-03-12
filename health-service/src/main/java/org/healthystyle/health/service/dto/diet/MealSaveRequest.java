package org.healthystyle.health.service.dto.diet;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import org.healthystyle.health.service.validation.annotation.XORNotNull;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@XORNotNull(fields = { "foodIds",
		"foodSetId" }, message = "Должна быть указана либо еда, либо набор еды, но не то и не другое")
public class MealSaveRequest {
	@NotEmpty(message = "Укажите хотя бы одно блюдо")
	private List<MealFoodSaveRequest> mealFoods;
	@NotNull(message = "Укажите время приёма пищи")
	private LocalTime time;
	@NotNull(message = "Укажите день приёма пищи")
	private Integer day;

	public MealSaveRequest() {
		super();
	}

	public MealSaveRequest(List<MealFoodSaveRequest> mealFoods, LocalTime time, Integer day) {
		super();
		this.mealFoods = mealFoods;
		this.time = time;
		this.day = day;
	}

	public List<MealFoodSaveRequest> getMealFoods() {
		return mealFoods;
	}

	public void setMealFoods(List<MealFoodSaveRequest> mealFoods) {
		this.mealFoods = mealFoods;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	@Override
	public int hashCode() {
		return Objects.hash(day, mealFoods, time);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MealSaveRequest other = (MealSaveRequest) obj;
		return Objects.equals(day, other.day) && Objects.equals(mealFoods, other.mealFoods)
				&& Objects.equals(time, other.time);
	}

}
