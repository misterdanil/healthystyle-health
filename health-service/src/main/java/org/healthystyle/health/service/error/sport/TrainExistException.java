package org.healthystyle.health.service.error.sport;

import java.time.LocalTime;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class TrainExistException extends AbstractException {
	private Integer day;
	private LocalTime time;

	public TrainExistException(Integer day, LocalTime time, BindingResult result) {
		super(result, "Train has already existed with day '%s' and time '%s'", day, time);
		this.day = day;
		this.time = time;
	}

	public Integer getDay() {
		return day;
	}

	public LocalTime getTime() {
		return time;
	}

}
