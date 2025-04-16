package org.healthystyle.health.service.dto.diet;

import java.time.LocalDate;

import org.healthystyle.health.service.validation.annotation.IsAfter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@IsAfter(beforeField = "start", afterField = "end", message = "Дата начала диеты должны быть позже даты конца")
public class DietUpdateRequest {
	@NotEmpty(message = "Укажите название для диеты")
	private String title;
	@NotNull(message = "Укажите дату начала диеты")
	private LocalDate start;
	@NotNull(message = "Укажите дату конца диеты")
	private LocalDate end;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

}
