package org.healthystyle.health.model;

import javax.annotation.processing.Generated;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table
public class Timezone {
	@Id
	@SequenceGenerator(name = "timezone_seq", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "timezone_seq", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false)
	private Long userId;
	@Column(nullable = false)
	private String timezone;

	public Timezone() {
		super();
	}

	public Timezone(Long userId, String timezone) {
		super();
		this.userId = userId;
		this.timezone = timezone;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

}
