package org.healthystyle.health.service.dto;

import org.healthystyle.health.model.measure.Type;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class IndicatorTypeUpdateRequest {
	@NotNull(message = "Должен быть указан идентификатор типа индикатора для его изменения")
	private Long id;
	@NotEmpty(message = "Должно быть введено название")
	private String name;
	@NotNull(message = "Должен быть указан тип измерения")
	private Type type;
	@NotNull(message = "Должен быть указан тип данных")
	private Long convertTypeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
