package org.healthystyle.health.repository.result;

import java.time.LocalDateTime;

public class AvgStatistic {
	private LocalDateTime createdOn;
	private String value;

	public AvgStatistic(LocalDateTime createdOn, String value) {
		super();
		this.createdOn = createdOn;
		this.value = value;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public String getValue() {
		return value;
	}

}
