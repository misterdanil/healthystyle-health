package org.healthystyle.health.service.error.medicine;

import java.time.LocalTime;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class IntakeExistException extends AbstractException {
	private LocalTime time;
	private Integer day;
	private Long planId;

	public IntakeExistException(LocalTime time, Integer day, Long planId, BindingResult result) {
		super(result, "Intake has already existed by time '%s', day '%s' and plan '%s'", time, day, planId);
		this.time = time;
		this.day = day;
		this.planId = planId;
	}

	public LocalTime getTime() {
		return time;
	}

	public Integer getDay() {
		return day;
	}

	public Long getPlanId() {
		return planId;
	}

}
