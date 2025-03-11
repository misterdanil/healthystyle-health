package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class NoFoodsException extends AbstractException {
	private Long mealId;

	public NoFoodsException(Long mealId, BindingResult result) {
		super(result, "There is no foods anymore for meal '%s'", mealId);
		this.mealId = mealId;
	}

	public Long getMealId() {
		return mealId;
	}

}
