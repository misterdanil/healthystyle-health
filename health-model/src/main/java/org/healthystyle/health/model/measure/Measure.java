package org.healthystyle.health.model.measure;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table
public class Measure {
	@Id
	@SequenceGenerator(name = "measure_generator", sequenceName = "measure_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "measure_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private Type type;

	public Measure() {
		super();
	}

	public Measure(Type type) {
		super();

		Objects.requireNonNull(type, "Type must be not null");

		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
