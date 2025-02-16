package org.healthystyle.health.model.medicine;

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
@Table(indexes = { @Index(name = "treatment_health_id_idx", columnList = "health_id"),
		@Index(name = "treatment_description_idx", columnList = "description") })
public class Treatment {
	@Id
	@SequenceGenerator(name = "treatment_generator", sequenceName = "treatment_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "treatment_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false, length = 1000)
	private String description;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "treatment")
	private List<Plan> plans;
	@ManyToOne
	@JoinColumn(name = "health_id", nullable = false)
	private Health health;

	public Treatment(String description, Health health, Plan... plans) {
		super();

		Objects.requireNonNull(description, "Description must be not null");
		Objects.requireNonNull(health, "Health must be not null");
		Objects.requireNonNull(plans, "Plans must be not null");

		this.description = description;
		this.plans = new ArrayList<>(Arrays.asList(plans));
		this.health = health;
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

	public List<Plan> getPlans() {
		if (plans == null) {
			plans = new ArrayList<>();
		}
		return plans;
	}

	public void addPlans(Plan... plans) {
		getPlans().addAll(Arrays.asList(plans));
	}

	public Health getHealth() {
		return health;
	}
}
