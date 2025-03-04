package org.healthystyle.health.service.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public class IndicatorUpdateRequest {
	@NotNull(message = "Укажите идентификатор показателя для изменения")
	private Long id;
	@NotNull(message = "Должно быть введено значение")
	private String value;
	@PastOrPresent(message = "Дата должна быть текущего или прошлого времени")
	private Instant date;

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

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

}
