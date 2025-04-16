package org.healthystyle.health.model.diet;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
@Table(indexes = @Index(name = "meal_time_day_idx", columnList = "time, day"))
public class Meal {
	@Id
	@SequenceGenerator(name = "meal_generator", sequenceName = "meal_seq", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "meal_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MealFood> foods;
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	private LocalTime time;
	@Column(nullable = false, columnDefinition = "SMALLINT CONSTRAINT CK_meal_day CHECK (day >= 1 AND day <= 7)")
	private Integer day;
	@ManyToOne
	@JoinColumn(name = "diet_id", nullable = false)
	private Diet diet;
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn = Instant.now();

	public Meal() {
		super();
	}

	public Meal(LocalTime time, Integer day, Diet diet) {
		super();

		Objects.requireNonNull(time, "Time must be not null");
		Objects.requireNonNull(day, "Day must be not null");
		Objects.requireNonNull(diet, "Diet must be not null");

		this.time = time;
		this.day = day;
		this.diet = diet;
	}

	public Meal(LocalTime time, Integer day, Diet diet, FoodSet foodSet, MealFood... foods) {
		super();

		Objects.requireNonNull(time, "Time must be not null");
		Objects.requireNonNull(day, "Day must be not null");
		Objects.requireNonNull(diet, "Diet must be not null");
		Objects.requireNonNull(foodSet, "Food set must be not null");
		Objects.requireNonNull(foods, "Foods must be not null");
		if (foods.length == 0) {
			throw new IllegalArgumentException("Must be passed at least one food");
		}

		this.time = time;
		this.day = day;
		this.diet = diet;
		this.foods = new LinkedHashSet<>(Arrays.asList(foods));
	}

	public Long getId() {
		return id;
	}

	public Set<MealFood> getFoods() {
		if (foods == null) {
			foods = new LinkedHashSet<>();
		}
		return foods;
	}

	public void removeFood(MealFood food) {
		getFoods().remove(food);
	}

	public void addFood(MealFood food) {
		getFoods().add(food);
	}

	public void addFoods(List<MealFood> foods) {
		getFoods().addAll(foods);
	}

	public void clearFoods() {
		getFoods().clear();
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Diet getDiet() {
		return diet;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}
}
