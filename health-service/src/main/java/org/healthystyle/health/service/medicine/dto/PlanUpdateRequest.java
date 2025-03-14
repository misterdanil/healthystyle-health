package org.healthystyle.health.service.medicine.dto;

import java.time.Instant;

import org.healthystyle.health.model.medicine.Sequence;
import org.healthystyle.health.service.validation.annotation.IsAfter;

import jakarta.validation.constraints.NotNull;

@IsAfter(beforeField = "start", afterField = "end", message = "Дата начала плана должны быть раньше даты конца")
public class PlanUpdateRequest {
	private Sequence sequence;
	@NotNull(message = "Укажите дату начала плана приёма лекарства")
	private Instant start;
	@NotNull(message = "Укажите дату конца плана приёма лекарства")
	private Instant end;

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
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
