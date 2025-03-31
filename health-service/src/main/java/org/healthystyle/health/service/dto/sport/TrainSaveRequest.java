package org.healthystyle.health.service.dto.sport;

import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class TrainSaveRequest {
	@NotBlank(message = "Укажите описание тренировки")
	private String description;
	@NotNull(message = "Укажите день")
	@Min(value = 1, message = "Минимальный день 1")
	@Max(value = 7, message = "Максимальный день 7")
	private Integer day;
	@NotNull(message = "Укажите время")
	private LocalTime time;
	@NotEmpty(message = "Укажите хотя бы одно упражнение")
	private List<SetSaveRequest> sets;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public List<SetSaveRequest> getSets() {
		return sets;
	}

	public void setSets(List<SetSaveRequest> sets) {
		this.sets = sets;
	}

}
