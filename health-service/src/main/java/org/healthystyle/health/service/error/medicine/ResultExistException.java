package org.healthystyle.health.service.error.medicine;

import java.time.LocalDate;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class ResultExistException extends AbstractException {
	private Long intakeId;
	private LocalDate date;

	public ResultExistException(Long intakeId, LocalDate date, BindingResult result) {
		super(result, "A result with intake '%s' and date '%s' has already existed", intakeId, date);
		this.intakeId = intakeId;
		this.date = date;
	}

	public Long getIntakeId() {
		return intakeId;
	}

	public LocalDate getDate() {
		return date;
	}

}
