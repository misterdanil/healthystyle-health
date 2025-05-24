package org.healthystyle.health.service.notifier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class NotificationSaveRequest {
	@NotBlank(message = "Укажите текст уведомления")
	private String title;
	@NotBlank(message = "Укажите тип уведомления")
	private String type;
	@NotNull(message = "Укажите пользователя-цель уведомления")
	private Long toUserId;
	@NotEmpty(message = "Укажите идентификатор")
	private String identifier;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

}
