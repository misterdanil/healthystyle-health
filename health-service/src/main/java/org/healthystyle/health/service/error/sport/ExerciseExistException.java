package org.healthystyle.health.service.error.sport;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class ExerciseExistException extends AbstractException {
	private String title;
	private Long healthId;

	public ExerciseExistException(String title, Long healthId, BindingResult result) {
		super(result, "A health has already had exercise with title '%s'", title);
		this.title = title;
		this.healthId = healthId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getHealthId() {
		return healthId;
	}

	public void setHealthId(Long healthId) {
		this.healthId = healthId;
	}

}
