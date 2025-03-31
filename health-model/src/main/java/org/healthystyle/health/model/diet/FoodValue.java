package org.healthystyle.health.model.diet;

import java.time.Instant;
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
@Table(name = "food_value", indexes = @Index(name = "food_value_food_id_idx", columnList = "food_id"))
public class FoodValue {
	@Id
	@SequenceGenerator(name = "food_value_generator", sequenceName = "food_value_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "food_value_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, columnDefinition = "VARCHAR(25) CONSTRAINT food_value_value_check CHECK (value ~ '^[0-9][0-9]*\\.?[0-9]+$')")
	private String value;
	@ManyToOne
	@JoinColumn(name = "nutrition_value_id", nullable = false)
	private NutritionValue nutritionValue;
	@ManyToOne
	@JoinColumn(name = "food_id", nullable = false)
	private Food food;
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;

	public FoodValue() {
		super();
	}

	public FoodValue(String value, NutritionValue nutritionValue, Food food) {
		super();

		Objects.requireNonNull(value, "Value must be not null");
		Objects.requireNonNull(nutritionValue, "Nutrition value must be not null");
		Objects.requireNonNull(food, "Food must be not null");

		this.value = value;
		this.nutritionValue = nutritionValue;
		this.food = food;
	}

	public Long getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public NutritionValue getNutritionValue() {
		return nutritionValue;
	}

	public Food getFood() {
		return food;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}
}
