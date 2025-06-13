package org.healthystyle.health.service.error;

import org.springframework.validation.BindingResult;

public class UserUnathenticatedException extends AbstractException {

	public UserUnathenticatedException(BindingResult result) {
		super("User is not authenticated for this operation", result);
	}

}
