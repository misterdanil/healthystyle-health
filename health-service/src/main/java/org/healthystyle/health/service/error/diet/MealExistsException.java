package org.healthystyle.health.service.error.diet;

import java.time.LocalTime;

import org.healthystyle.health.model.diet.Meal;
import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class MealExistsException extends AbstractException {
	private Long dietId;
	private Integer day;
	private LocalTime time;
	private Meal meal;

	public MealExistsException(BindingResult result, Long dietId, Integer day, LocalTime time, Meal meal) {
		super(result, "There is already a meal '%s' at day %s and time %s for diet '%s'", meal, day, time, dietId);
		this.dietId = dietId;
		this.day = day;
		this.time = time;
		this.meal = meal;
	}

	public Long getDietId() {
		return dietId;
	}

	public Integer getDay() {
		return day;
	}

	public LocalTime getTime() {
		return time;
	}

	public Meal getMeal() {
		return meal;
	}

}
