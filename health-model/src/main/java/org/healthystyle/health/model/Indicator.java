package org.healthystyle.health.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(indexes = { @Index(name = "indicator_indicator_type_id_idx", columnList = "indicator_type_id"),
		@Index(name = "indicator_created_on_idx", columnList = "createdOn") })
public class Indicator {
	@Id
	@SequenceGenerator(name = "indicator_generator", sequenceName = "indicator_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "indicator_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, columnDefinition = "VARCHAR(100) CONSTRAINT indicator_value_check CHECK (value ~ '^[0-9][0-9]*\\.?[0-9]+$')")
	private String value;
	@ManyToOne
	@JoinColumn(name = "indicator_type_id", nullable = false)
	private IndicatorType indicatorType;
	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;
	@ManyToOne
	@JoinColumn(name = "health_id", nullable = false)
	private Health health;

	public Indicator() {
		super();
	}

	public Indicator(String value, IndicatorType indicatorType, LocalDateTime createdOn, Health health) {
		super();

		Objects.requireNonNull(value, "Value must be not null");
		Objects.requireNonNull(indicatorType, "Indicator type must be not null");
		Objects.requireNonNull(createdOn, "Created on must be not null");
		Objects.requireNonNull(health, "Health must be not null");

		this.value = value;
		this.indicatorType = indicatorType;
		this.createdOn = createdOn;
		this.health = health;
	}

	public Long getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public IndicatorType getIndicatorType() {
		return indicatorType;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public Health getHealth() {
		return health;
	}
}
