package org.healthystyle.health.service.error.diet;

import java.util.Arrays;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class FoodNotFoundException extends AbstractException {
	private Long[] ids;

	public FoodNotFoundException(BindingResult result, Long... ids) {
		super(result, "Could not found food by id: %s", Arrays.asList(ids));
		this.ids = ids;
	}

	public Long[] getIds() {
		return ids;
	}

}
