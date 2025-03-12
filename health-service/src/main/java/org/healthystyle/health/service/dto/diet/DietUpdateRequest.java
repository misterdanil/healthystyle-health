package org.healthystyle.health.service.dto.diet;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.healthystyle.health.service.validation.annotation.IsAfter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@IsAfter(beforeField = "start", afterField = "end", message = "Дата начала диеты должны быть позже даты конца")
public class DietUpdateRequest {
	@NotEmpty(message = "Укажите название для диеты")
	private String title;
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
