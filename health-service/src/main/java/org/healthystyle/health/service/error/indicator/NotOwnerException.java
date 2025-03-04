package org.healthystyle.health.service.error.indicator;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class NotOwnerException extends AbstractException {
	private Long indicatorId;
	private Long healthId;

	public NotOwnerException(Long indicatorId, Long healthId, BindingResult result) {
		super(result, "%s health is not owner of the indicator %s", healthId, indicatorId);
		this.indicatorId = indicatorId;
		this.healthId = healthId;
	}

	public Long getIndicatorId() {
		return indicatorId;
	}

	public Long getHealthId() {
		return healthId;
	}

}
