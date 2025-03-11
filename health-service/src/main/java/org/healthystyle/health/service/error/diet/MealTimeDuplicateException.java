package org.healthystyle.health.service.error.diet;

import java.time.Instant;
import java.util.Set;

import org.healthystyle.health.service.dto.diet.MealSaveRequest;
import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class MealTimeDuplicateException extends AbstractException {
	private Set<MealSaveRequest> meals;

	public MealTimeDuplicateException(Set<MealSaveRequest> meals, BindingResult result) {
		super(result, "Found duplicates by diet id, day and time: %s", meals);
		this.meals = meals;
	}

	public Set<MealSaveRequest> getMeals() {
		return meals;
	}

}
