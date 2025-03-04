package org.healthystyle.health.service.error.indicator;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class NotCreatorException extends AbstractException {
	private Long id;
	private Long creator;

	public NotCreatorException(Long id, Long creator, BindingResult result) {
		super(result, "This user '%s' is not creator of indicator type '%s'", creator, id);
		this.id = id;
		this.creator = creator;
	}

	public Long getId() {
		return id;
	}

	public Long getCreator() {
		return creator;
	}

}
