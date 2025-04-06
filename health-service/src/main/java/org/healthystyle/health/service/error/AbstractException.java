package org.healthystyle.health.service.error;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public abstract class AbstractException extends Exception {
	private BindingResult result;
	private List<String> globalErrors;
	private Map<String, String> fieldErrors;
	private String template;
	private Object[] args;

	public AbstractException(String message, BindingResult result, Object... args) {
		super(String.format(message, args));
		this.result = result;
		globalErrors = getGlobalErrorsValues(result.getGlobalErrors());
		fieldErrors = getFieldErrorsValues(result.getFieldErrors());
	}

	public AbstractException(BindingResult result, String template, Object... args) {
		super();
		this.result = result;
		globalErrors = getGlobalErrorsValues(result.getGlobalErrors());
		fieldErrors = getFieldErrorsValues(result.getFieldErrors());
		this.template = template;
		this.args = args;
	}

	public AbstractException(BindingResult result, String template, Throwable cause, Object... args) {
		super(cause);
		this.result = result;
		globalErrors = getGlobalErrorsValues(result.getGlobalErrors());
		fieldErrors = getFieldErrorsValues(result.getFieldErrors());
		this.template = template;
		this.args = args;
	}

	public BindingResult getResult() {
		return result;
	}

	private List<String> getGlobalErrorsValues(List<ObjectError> globalErrors) {
		List<String> globalErrorsValues = new ArrayList<>();
		globalErrors.forEach(ge -> globalErrorsValues.add(ge.getDefaultMessage()));

		return globalErrorsValues;
	}

	private Map<String, String> getFieldErrorsValues(List<FieldError> fieldErrors) {
		Map<String, String> fieldErrorsValues = new LinkedHashMap<>();
		fieldErrors.forEach(fe -> fieldErrorsValues.put(fe.getField(), fe.getDefaultMessage()));

		return fieldErrorsValues;
	}

	@Override
	public String getMessage() {
		if (template != null) {
			return String.format(template, args);
		} else {
			return super.getMessage();
		}
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
