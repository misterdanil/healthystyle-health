package org.healthystyle.health.model.sport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.healthystyle.health.model.Health;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(indexes = @Index(name = "exercise_title_idx", columnList = "title"))
public class Exercise {
	@Id
	@SequenceGenerator(name = "exercise_id", sequenceName = "exercise_seq", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "exercise_id", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, length = 1000)
	private String title;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "exercise")
	private List<Step> steps;
	@ManyToOne
	@JoinColumn(name = "health_id", nullable = false)
	private Health health;

	public Exercise() {
		super();
	}

	public Exercise(String title, Health health) {
		super();

		Objects.requireNonNull(title, "Title must be not null");
		Objects.requireNonNull(health, "Health must be not null");
		Objects.requireNonNull(steps, "Steps must be not null");
//		if (steps.length == 0) {
//			throw new IllegalArgumentException("Must be passed at least one step");
//		}

		this.title = title;
//		this.steps = new ArrayList<>(Arrays.asList(steps));
		this.health = health;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Step> getSteps() {
		if (steps == null) {
			steps = new ArrayList<>();
		}
		return steps;
	}

	public void addStep(Step step) {
		getSteps().add(step);
	}

	public void addSteps(List<Step> steps) {
		getSteps().addAll(steps);
	}

	public Health getHealth() {
		return health;
	}
}
