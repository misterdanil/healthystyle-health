package org.healthystyle.health.model.measure.convert;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "convert_type")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
public abstract class ConvertType {
	@Id
	@SequenceGenerator(name = "convert_type_id_generator", sequenceName = "convert_type_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "convert_type_id_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(name = "min_value")
	private BigDecimal minValue;
	@Column(name = "max_value")
	private BigDecimal maxValue;
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;

	public ConvertType() {
		super();
	}

	public ConvertType(BigDecimal minValue, BigDecimal maxValue) {
		super();
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Instant createdOn) {
		this.createdOn = createdOn;
	}

}
