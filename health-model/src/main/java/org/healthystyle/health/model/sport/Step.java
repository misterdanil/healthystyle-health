package org.healthystyle.health.model.sport;

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
@Table(indexes = @Index(name = "step_exercise_id_idx", columnList = "exercise_id"))
public class Step {
	@Id
	@SequenceGenerator(name = "step_generator", sequenceName = "step_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "step_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, length = 2000)
	private String description;
	@ManyToOne
	@JoinColumn(name = "exercise_id", nullable = false)
	private Exercise exercise;

	public Step() {
		super();
	}

	public Step(String description, Exercise exercise) {
		super();

		Objects.requireNonNull(description, "Description must be not null");
		Objects.requireNonNull(exercise, "Exercise must be not null");

		this.description = description;
		this.exercise = exercise;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Exercise getExercise() {
		return exercise;
	}
}
