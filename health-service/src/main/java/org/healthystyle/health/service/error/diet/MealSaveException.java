package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.dto.diet.MealSaveRequest;
import org.healthystyle.health.service.error.AbstractException;

public class MealSaveException extends AbstractException {
	private Long dietId;
	private MealSaveRequest saveRequest;

	public MealSaveException(MealSaveRequest saveRequest, Long dietId, AbstractException cause) {
		super(cause.getResult(), "Could not save meal '%s' for diet '%s'", saveRequest, dietId);
		this.dietId = dietId;
		this.saveRequest = saveRequest;
	}

	public Long getDietId() {
		return dietId;
	}

	public MealSaveRequest getSaveRequest() {
		return saveRequest;
	}
}
