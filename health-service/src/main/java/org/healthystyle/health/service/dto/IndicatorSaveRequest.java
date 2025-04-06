package org.healthystyle.health.service.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public class IndicatorSaveRequest {
	@NotEmpty(message = "Укажите значение для показателя здоровья")
	private String value;
	@NotNull(message = "Укажите показатель здоровья для которого вы хотите ввести значение")
	private Long indicatorTypeId;
	@PastOrPresent(message = "Дата и время должны быть настоящего или прошлого времени")
	private LocalDateTime date;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getIndicatorTypeId() {
		return indicatorTypeId;
	}

	public void setIndicatorTypeId(Long indicatorTypeId) {
		this.indicatorTypeId = indicatorTypeId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

}
