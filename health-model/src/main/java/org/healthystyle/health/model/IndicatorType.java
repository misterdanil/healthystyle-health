package org.healthystyle.health.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.convert.ConvertType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "indicator_type", indexes = @Index(name = "indicator_type_name_idx", columnList = "name", unique = true))
public class IndicatorType {
	@Id
	@SequenceGenerator(name = "indicator_type_generator", sequenceName = "indicator_type_sequence", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "indicator_type_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, columnDefinition = "VARCHAR(500) CONSTRAINT CK_indicator_type_name CHECK (name ~ '^[А-Я][а-я 0-9]+$')")
	private String name;
	@ManyToOne
	@JoinColumn(name = "measure_id", nullable = false)
	private Measure measure;
	@ManyToOne
	@JoinColumn(name = "convert_type_id", nullable = false)
	private ConvertType convertType;
	@Column(nullable = false)
	private Long creator;
	@OneToMany(mappedBy = "indicatorType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Indicator> indicators;
	@Column(nullable = false)
	private Instant createdOn = Instant.now();

	public IndicatorType() {
		super();
	}

	public IndicatorType(String name, Measure measure, ConvertType convertType, Long creator) {
		super();

		Objects.requireNonNull(name, "Name must be not null");
		Objects.requireNonNull(measure, "Measure must be not null");
		Objects.requireNonNull(convertType, "Convert type must be not null");
		Objects.requireNonNull(creator, "Creator must be not null");

		this.name = name;
		this.measure = measure;
		this.convertType = convertType;
		this.creator = creator;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getCreator() {
		return creator;
	}

	public List<Indicator> getIndicators() {
		if (indicators == null) {
			indicators = new ArrayList<>();
		}
		return indicators;
	}

	public void addIndicators(List<Indicator> indicators) {
		getIndicators().addAll(indicators);
	}

	public void addIndicator(Indicator indicator) {
		getIndicators().add(indicator);
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

}
