package org.healthystyle.health.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(indexes = @Index(name = "timezone_user_id_idx", columnList = "user_id", unique = true))
public class Timezone implements Serializable {
	@Id
	@SequenceGenerator(name = "timezone_id_generator", sequenceName = "timezone_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "timezone_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, unique = true)
	private Long userId;
	@Column(nullable = false)
	private String timezone;
	
	@Transient
	private static final long serialVersionUID = -6358052541403835918L;

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
