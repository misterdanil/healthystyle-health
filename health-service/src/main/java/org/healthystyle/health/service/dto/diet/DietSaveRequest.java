package org.healthystyle.health.service.dto.diet;

import java.time.Instant;
import java.util.List;

import org.healthystyle.health.service.validation.annotation.IsAfter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@IsAfter(beforeField = "start", afterField = "end", message = "Дата конца дтеты должны быть позже даты начала")
public class DietSaveRequest {
	@NotEmpty(message = "Укажите заголовок для диеты")
	private String title;
	@NotEmpty(message = "Должен быть указан хотя бы один приём пищи")
	private List<MealSaveRequest> meals;
	@NotNull(message = "Укажите дату начала диеты")
	private Instant start;
	@NotNull(message = "Укажите дату конца диеты")
	private Instant end;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<MealSaveRequest> getMeals() {
		return meals;
	}

	public void setMeals(List<MealSaveRequest> meals) {
		this.meals = meals;
	}

	public Instant getStart() {
		return start;
	}

	public void setStart(Instant start) {
		this.start = start;
	}

	public Instant getEnd() {
		return end;
	}

	public void setEnd(Instant end) {
		this.end = end;
	}

}
