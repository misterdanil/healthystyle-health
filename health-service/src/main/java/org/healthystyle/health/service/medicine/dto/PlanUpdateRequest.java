package org.healthystyle.health.service.medicine.dto;

import java.time.LocalDate;

import org.healthystyle.health.model.medicine.Sequence;
import org.healthystyle.health.service.validation.annotation.IsAfter;

import jakarta.validation.constraints.NotNull;

@IsAfter(beforeField = "start", afterField = "end", message = "Дата начала плана должны быть раньше даты конца")
public class PlanUpdateRequest {
	private Sequence sequence;
	@NotNull(message = "Укажите дату начала плана приёма лекарства")
	private LocalDate start;
	@NotNull(message = "Укажите дату конца плана приёма лекарства")
	private LocalDate end;

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
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
