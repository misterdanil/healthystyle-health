package org.healthystyle.health.service.error.sport;

import java.time.LocalDate;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class ResultExistException extends AbstractException {
	private Long setId;
	private Integer setNumber;
	private LocalDate date;

	public ResultExistException(Long setId, Integer setNumber, LocalDate date, BindingResult result) {
		super(result, "Result has already existed by set id '%s', set number '%s' and date '%s'", setId, setNumber,
				date);
		this.setId = setId;
		this.setNumber = setNumber;
		this.date = date;
	}

	public Long getSetId() {
		return setId;
	}

	public Integer getSetNumber() {
		return setNumber;
	}

	public LocalDate getDate() {
		return date;
	}

}
