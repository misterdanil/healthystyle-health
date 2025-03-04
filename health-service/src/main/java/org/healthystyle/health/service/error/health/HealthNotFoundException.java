package org.healthystyle.health.service.error.health;

import org.healthystyle.health.service.error.AbstractException;
import org.healthystyle.health.service.error.Field;
import org.springframework.validation.BindingResult;

public class HealthNotFoundException extends AbstractException {
	private Field<?> field;

	public HealthNotFoundException(Field<?> field, BindingResult result) {
		super(result, "A health was not found by '%s' and value '%s'",
				new Object[] { field.getName(), field.getValue() });
		this.field = field;
	}

	public Field<?> getField() {
		return field;
	}
}
