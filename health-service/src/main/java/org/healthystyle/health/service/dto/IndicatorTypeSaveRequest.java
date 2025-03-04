package org.healthystyle.health.service.dto;

import org.healthystyle.health.model.measure.Type;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class IndicatorTypeSaveRequest {
	@NotEmpty(message = "Укажите название для типа индикатора")
	private String name;
	@NotNull(message = "Должно быть указано измерение")
	private Type type;
	@NotNull(message = "Укажите тип для ввода пользователями данных")
	private Long convertTypeId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Long getConvertTypeId() {
		return convertTypeId;
	}

	public void setConvertTypeId(Long convertTypeId) {
		this.convertTypeId = convertTypeId;
	}

}
