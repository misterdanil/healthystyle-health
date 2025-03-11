package org.healthystyle.health.model.diet;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
	@ManyToMany
	@JoinTable(name = "meal_food", joinColumns = @JoinColumn(name = "meal_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "food_id", nullable = false))
	private Set<Food> foods;
	@ManyToOne
	@JoinColumn(name = "food_set_id", nullable = false)
	private FoodSet foodSet;
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	private LocalTime time;
	@Column(nullable = false, columnDefinition = "SMALLINT CONSTRAINTS meal_day_check CHECK (day >= 0 AND day <= 6)")
	private Integer day;
	@ManyToOne
	@JoinColumn(name = "diet_id", nullable = false)
	private Diet diet;
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;

	public Meal() {
		super();
	}

	public Meal(LocalTime time, Integer day, Diet diet, Food... foods) {
		super();

		Objects.requireNonNull(time, "Time must be not null");
		Objects.requireNonNull(day, "Day must be not null");
		Objects.requireNonNull(diet, "Diet must be not null");
		Objects.requireNonNull(foods, "Foods must be not null");
		if (foods.length == 0) {
			throw new IllegalArgumentException("Must be passed at least one food");
		}

		this.foods = new LinkedHashSet<>(Arrays.asList(foods));
		this.time = time;
		this.day = day;
		this.diet = diet;
	}

	public Meal(LocalTime time, Integer day, Diet diet, FoodSet foodSet) {
		super();

		Objects.requireNonNull(time, "Time must be not null");
		Objects.requireNonNull(day, "Day must be not null");
		Objects.requireNonNull(diet, "Diet must be not null");
		Objects.requireNonNull(foodSet, "Food set must be not null");

		this.time = time;
		this.day = day;
		this.diet = diet;
		this.foodSet = foodSet;
	}

	public Long getId() {
		return id;
	}

	public Set<Food> getFoods() {
		return foods;
	}

	public void removeFood(Food food) {
		getFoods().remove(food);
	}

	public void addFood(Food food) {
		getFoods().add(food);
	}
	
	public void addFoods(List<Food> foods) {
		getFoods().addAll(foods);
	}
	
	public void clearFoods() {
		getFoods().clear();
	}

	public FoodSet getFoodSet() {
		return foodSet;
	}

	public void setFoodSet(FoodSet foodSet) {
		this.foodSet = foodSet;
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
