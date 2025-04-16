package org.healthystyle.health.model.diet;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(indexes = { @Index(name = "diet_title_idx", columnList = "title", unique = true),
		@Index(name = "diet_start_finish_idx", columnList = "start, finish") })
public class Diet {
	@Id
	@SequenceGenerator(name = "diet_generator", sequenceName = "diet_seq", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "diet_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, unique = true)
	private String title;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "diet")
	private List<Meal> meals;
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private LocalDate start;
	@Temporal(TemporalType.DATE)
	@Column(name = "finish", nullable = false)
	private LocalDate end;
	@ManyToOne
	@JoinColumn(name = "health_id", nullable = false)
	private Health health;
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn = Instant.now();

	public Diet() {
		super();
	}

	public Diet(String title, LocalDate start, LocalDate end, Health health) {
		super();

		Objects.requireNonNull(title, "Title must be not null");
		Objects.requireNonNull(start, "Start must be not null");
		Objects.requireNonNull(end, "End must be not null");
		Objects.requireNonNull(health, "Health must be not null");

		this.title = title;
		this.start = start;
		this.end = end;
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

	public List<Meal> getMeals() {
		if(meals == null) {
			meals = new ArrayList<>();
		}
		return meals;
	}

	public void addMeal(Meal meal) {
		getMeals().add(meal);
	}

	public void removeMeal(Meal meal) {
		getMeals().remove(meal);
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public Health getHealth() {
		return health;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}
}
