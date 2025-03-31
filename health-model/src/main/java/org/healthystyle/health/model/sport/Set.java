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
@Table(indexes = @Index(name = "set_train_id_idx", columnList = "train_id"))
public class Set {
	@Id
	@SequenceGenerator(name = "set_generator", sequenceName = "set_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "set_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "exercise_id", nullable = false)
	private Exercise exercise;
	@Column(nullable = false)
	private Integer count;
	@Column(nullable = false)
	private Integer repeat;
	@ManyToOne
	@JoinColumn(name = "train_id", nullable = false)
	private Train train;

	public Set() {
		super();
	}

	public Set(Exercise exercise, Integer count, Integer repeat, Train train) {
		super();

		Objects.requireNonNull(exercise, "Exercise must be not null");
		Objects.requireNonNull(count, "Count must be not null");
		Objects.requireNonNull(repeat, "Repeat must be not null");
		Objects.requireNonNull(train, "Train must be not null");

		this.exercise = exercise;
		this.count = count;
		this.repeat = repeat;
		this.train = train;
	}

	public Long getId() {
		return id;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getRepeat() {
		return repeat;
	}

	public void setRepeat(Integer repeat) {
		this.repeat = repeat;
	}

	public Train getTrain() {
		return train;
	}

}
