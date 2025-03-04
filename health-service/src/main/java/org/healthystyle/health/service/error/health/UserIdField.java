package org.healthystyle.health.service.error.health;

import org.healthystyle.health.service.error.Field;

public class UserIdField extends Field<Long> {

	public UserIdField(Long value) {
		super(value);
	}

	@Override
	public String getName() {
		return "userId";
	}

}
