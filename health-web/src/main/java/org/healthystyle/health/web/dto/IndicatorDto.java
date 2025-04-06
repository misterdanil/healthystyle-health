package org.healthystyle.health.web.dto;

import java.time.LocalDateTime;

public class IndicatorDto {
	private Long id;
	private String value;
	private IndicatorTypeDto indicatorType;
	private LocalDateTime createdOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public IndicatorTypeDto getIndicatorType() {
		return indicatorType;
	}

	public void setIndicatorType(IndicatorTypeDto indicatorType) {
		this.indicatorType = indicatorType;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

}
