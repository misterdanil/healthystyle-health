package org.healthystyle.health.model.diet;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.convert.ConvertType;

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
@Table(indexes = @Index(name = "food_title_idx", columnList = "title"))
public class Food {
	@Id
	@SequenceGenerator(name = "food_generator", sequenceName = "food_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "food_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, unique = true)
	private String title;
	private String weight;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "food")
	private List<FoodValue> foodValues;
	@ManyToOne
	@JoinColumn(name = "measure_id")
	private Measure measure;
	@ManyToOne
	@JoinColumn(name = "convert_type_id")
	private ConvertType convertType;
	@ManyToOne
	@JoinColumn(name = "health_id", nullable = false)
	private Health health;
	@Column(name = "created_on", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn = Instant.now();

	public Food() {
		super();
	}

	public Food(String title, Health health) {
		super();

		Objects.requireNonNull(title, "Title must be not null");
		Objects.requireNonNull(health, "Health must be not null");

		this.title = title;
		this.health = health;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public void addFoodValue(FoodValue foodValue) {
		getFoodValues().add(foodValue);
	}

	public List<FoodValue> getFoodValues() {
		if(foodValues == null) {
			foodValues = new ArrayList<>();
		}
		return foodValues;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public ConvertType getConvertType() {
		return convertType;
	}

	public void setConvertType(ConvertType convertType) {
		this.convertType = convertType;
	}

	public Health getHealth() {
		return health;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}
}
