package org.healthystyle.health.model.diet;

import java.time.Instant;
import java.util.Objects;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.convert.ConvertType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "nutrition_value", indexes = @Index(name = "nutrition_value_value_idx", columnList = "value", unique = true))
public class NutritionValue {
	@Id
	@SequenceGenerator(name = "nutrition_value_generator", sequenceName = "nutrition_value_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "nutrition_value_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private Value value;
	@ManyToOne
	@JoinColumn(name = "measure_id", nullable = false)
	private Measure measure;
	@ManyToOne
	@JoinColumn(name = "convert_type_id", nullable = false)
	private ConvertType convertType;
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;

	public NutritionValue() {
		super();
	}

	public NutritionValue(Value value, Measure measure, ConvertType convertType) {
		super();

		Objects.requireNonNull(value, "Value must be not null");
		Objects.requireNonNull(measure, "Measure must be not null");
		Objects.requireNonNull(convertType, "Convert type must be not null");

		this.value = value;
		this.measure = measure;
		this.convertType = convertType;
	}

	public Long getId() {
		return id;
	}

	public Value getValue() {
		return value;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public ConvertType getConvertType() {
		return convertType;
	}

	public void setConvertType(ConvertType convertType) {
		this.convertType = convertType;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

}
