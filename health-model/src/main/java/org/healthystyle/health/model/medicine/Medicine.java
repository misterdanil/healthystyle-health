package org.healthystyle.health.model.medicine;

import java.time.Instant;
import java.util.Objects;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.convert.ConvertType;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(indexes = @Index(name = "medicine_name_health_id_idx", columnList = "name, health_id", unique = true))
public class Medicine {
	@Id
	@SequenceGenerator(name = "medicine_generator", sequenceName = "medicine_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "medicine_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false)
	private String name;
	private String weight;
	@ManyToOne
	@JoinColumn(name = "convert_type_id")
	private ConvertType convertType;
	@ManyToOne
	@JoinColumn(name = "measure_id")
	private Measure measure;
	@ManyToOne
	@JoinColumn(name = "health_id", nullable = false)
	private Health health;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = false)
	private Instant createdOn = Instant.now();

	public Medicine() {
		super();
	}

	public Medicine(String name, Health health) {
		super();

		Objects.requireNonNull(name, "Name must be not null");
		Objects.requireNonNull(health, "Health must be not null");

		this.name = name;
		this.health = health;
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

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public ConvertType getConvertType() {
		return convertType;
	}

	public void setConvertType(ConvertType convertType) {
		this.convertType = convertType;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public Health getHealth() {
		return health;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}
}
