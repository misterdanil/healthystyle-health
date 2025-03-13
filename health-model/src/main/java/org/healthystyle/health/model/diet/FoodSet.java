package org.healthystyle.health.model.diet;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.healthystyle.health.model.Health;

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

@Entity
@Table(name = "food_set", indexes = @Index(name = "food_set_title_idx", columnList = "title", unique = true))
public class FoodSet {
	@Id
	@SequenceGenerator(name = "food_set_generator", sequenceName = "food_set_seq", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "food_set_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, unique = true)
	private String title;
	@ManyToMany
	@JoinTable(name = "food_set_food", joinColumns = @JoinColumn(name = "food_set_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "food_id", nullable = false))
	private List<Food> foods;
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;
	@ManyToOne
	@JoinColumn(name = "health_id", nullable = false)
	private Health health;

	public FoodSet() {
		super();
	}

	public FoodSet(String title, Health health, List<Food> foods) {
		super();

		Objects.requireNonNull(title, "Title must be not null");
		Objects.requireNonNull(health, "Health must be not null");
		Objects.requireNonNull(foods, "Foods must be not null");
		if (foods.size() == 0) {
			throw new IllegalArgumentException("Must be passed at least one food");
		}

		this.title = title;
		this.foods = foods;
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

	public List<Food> getFoods() {
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

	public Instant getCreatedOn() {
		return createdOn;
	}

	public Health getHealth() {
		return health;
	}
}
