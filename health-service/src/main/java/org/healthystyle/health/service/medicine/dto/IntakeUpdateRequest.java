package org.healthystyle.health.service.medicine.dto;

import java.time.LocalTime;

import org.healthystyle.health.model.measure.Type;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class IntakeUpdateRequest {
	@NotNull(message = "Укажите время приёма лекарства")
	private LocalTime time;
	@NotNull(message = "Укажите день приёма лекарства")
	@Min(value = 0, message = "Минимальное значение для дня - 0")
	@Max(value = 6, message = "Максимальное значение для дня - 6")
	private Integer day;
	private String weight;
	private Type measure;

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

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Type getMeasure() {
		return measure;
	}

	public void setMeasure(Type measure) {
		this.measure = measure;
	}

}
