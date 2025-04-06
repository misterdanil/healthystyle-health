package org.healthystyle.health.web.dto;

import java.util.List;
import java.util.Map;

public class ErrorResponse {
	private List<String> globalErrors;
	private Map<String, String> fieldErrors;

	public ErrorResponse(List<String> globalErrors, Map<String, String> fieldErrors) {
		super();
		this.globalErrors = globalErrors;
		this.fieldErrors = fieldErrors;
	}

	public List<String> getGlobalErrors() {
		return globalErrors;
	}

	public void setGlobalErrors(List<String> globalErrors) {
		this.globalErrors = globalErrors;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(Map<String, String> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

}
