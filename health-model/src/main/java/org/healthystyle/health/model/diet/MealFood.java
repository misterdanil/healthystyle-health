package org.healthystyle.health.model.diet;

import java.time.Instant;
import java.util.Objects;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.convert.ConvertType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "meal_food")
public class MealFood {
	@Id
	@SequenceGenerator(name = "meal_food_generator", sequenceName = "meal_food_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "meal_food_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "meal_id", nullable = false)
	private Meal meal;
	@ManyToOne
	@JoinColumn(name = "food_id", nullable = false)
	private Food food;
	@Column(name = "weight", nullable = false)
	private Float weight;
	@ManyToOne
	@JoinColumn(name = "measure_id", nullable = false)
	private Measure measure;
//	@ManyToOne
//	@JoinColumn(name = "convert_type_id", nullable = false)
//	private ConvertType convertType;
	@Column(name = "created_on", nullable = false)
	private Instant createdOn;

	public MealFood() {
		super();
	}

	public MealFood(Meal meal, Food food, Float weight, Measure measure) {
		super();

		Objects.requireNonNull(meal, "Meal must be not null");
		Objects.requireNonNull(food, "Food must be not null");
		Objects.requireNonNull(weight, "Weight must be not null");
		Objects.requireNonNull(measure, "Measure must be not null");
//		Objects.requireNonNull(convertType, "Convert type must be not null");

		this.meal = meal;
		this.food = food;
		this.weight = weight;
		this.measure = measure;
//		this.convertType = convertType;
		this.createdOn = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public Meal getMeal() {
		return meal;
	}

	public Food getFood() {
		return food;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

//	public ConvertType getConvertType() {
//		return convertType;
//	}
//
//	public void setConvertType(ConvertType convertType) {
//		this.convertType = convertType;
//	}

	public Instant getCreatedOn() {
		return createdOn;
	}
}
