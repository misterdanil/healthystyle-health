package org.healthystyle.health.service.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public class IndicatorUpdateRequest {
	@NotNull(message = "Укажите идентификатор показателя для изменения")
	private Long id;
	@NotNull(message = "Должно быть введено значение")
	private String value;
	@PastOrPresent(message = "Дата должна быть текущего или прошлого времени")
	private LocalDateTime date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

}
