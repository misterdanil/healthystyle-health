package org.healthystyle.health.web.measure.dto;

import org.healthystyle.health.model.measure.Type;

public class MeasureDto {
	private Long id;
	private Type type;
	private String translate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTranslate() {
		return translate;
	}

	public void setTranslate(String translate) {
		this.translate = translate;
	}

}
