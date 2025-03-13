package org.healthystyle.health.service.error.diet;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class FoodSetNoFoodsException extends AbstractException {
	private Long foodSetId;

	public FoodSetNoFoodsException(Long foodSetId, BindingResult result) {
		super(result, "There is no foods anymore in food set '%s'", foodSetId);
		this.foodSetId = foodSetId;
	}

	public Long getFoodSetId() {
		return foodSetId;
	}

}
