package org.healthystyle.health.service.dto.sport;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class ResultSaveRequest {
	@NotNull(message = "Укажите номер подхода")
	@Positive(message = "Номер подхода должен быть положительным")
	private Integer setNumber;
	@NotNull(message = "Укажите количество повторений")
	@Positive(message = "Количество повтоений должено быть положительным")
	private Integer actualRepeat;
	@NotNull(message = "Укажите дату")
	@PastOrPresent(message = "Дата должна быть текущей или прошлой")
	private LocalDate date;

	public Integer getSetNumber() {
		return setNumber;
	}

	public void setSetNumber(Integer setNumber) {
		this.setNumber = setNumber;
	}

	public Integer getActualRepeat() {
		return actualRepeat;
	}

	public void setActualRepeat(Integer actualRepeat) {
		this.actualRepeat = actualRepeat;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
