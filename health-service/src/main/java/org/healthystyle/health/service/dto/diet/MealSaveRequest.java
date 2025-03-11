package org.healthystyle.health.service.dto.diet;

import java.time.Instant;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.healthystyle.health.service.error.diet.MealNotFoundException;
import org.healthystyle.health.service.error.diet.MealSaveException;
import org.healthystyle.health.service.validation.annotation.XORNotNull;
import org.springframework.validation.MapBindingResult;

import jakarta.validation.constraints.NotNull;

@XORNotNull(fields = { "foodIds",
		"foodSetId" }, message = "Должна быть указана либо еда, либо набор еды, но не то и не другое")
public class MealSaveRequest {
	private List<Long> foodIds;
	private Long foodSetId;
	@NotNull(message = "Укажите время приёма пищи")
	private LocalTime time;
	@NotNull(message = "Укажите день приёма пищи")
	private Integer day;

	public List<Long> getFoodIds() {
		return foodIds;
	}

	public void setFoodIds(List<Long> foodIds) {
		this.foodIds = foodIds;
	}

	public Long getFoodSetId() {
		return foodSetId;
	}

	public void setFoodSetId(Long foodSetId) {
		this.foodSetId = foodSetId;
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
		return Objects.hash(day, foodIds, foodSetId, time);
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
		return Objects.equals(day, other.day) && Objects.equals(foodIds, other.foodIds)
				&& Objects.equals(foodSetId, other.foodSetId) && Objects.equals(time, other.time);
	}
	

	public static void main(String[] args) throws MealSaveException {
		MealSaveException e = new MealSaveException();
		throw e;
	}

}
