package org.healthystyle.health.model.sport;

import java.time.Instant;
import java.time.LocalDate;
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
@Table(name = "set_result", indexes = @Index(name = "set_result_set_id_idx", columnList = "set_id"))
public class Result {
	@Id
	@SequenceGenerator(name = "set_result_generator", sequenceName = "set_result_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "set_result_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "set_id", nullable = false)
	private Set set;
	@Column(name = "set_number", nullable = false)
	private Integer setNumber;
	@Column(name = "actual_repeat", nullable = false)
	private Integer actualRepeat;
	@Column(name = "executed_date", nullable = false)
	private LocalDate date;
	@Column(name = "created_on", nullable = false)
	private Instant createdOn = Instant.now();

	public Result() {
		super();
	}

	public Result(Set set, Integer setNumber, Integer actualRepeat, LocalDate date) {
		super();

		Objects.requireNonNull(set, "Set must be not null");
		Objects.requireNonNull(setNumber, "Set number must be not null");
		Objects.requireNonNull(actualRepeat, "Actual repeat must be not null");
		Objects.requireNonNull(date, "Date must be not null");

		this.set = set;
		this.setNumber = setNumber;
		this.actualRepeat = actualRepeat;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public Set getSet() {
		return set;
	}

	public Integer getSetNumber() {
		return setNumber;
	}

	public Integer getActualRepeat() {
		return actualRepeat;
	}

	public LocalDate getDate() {
		return date;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

}
