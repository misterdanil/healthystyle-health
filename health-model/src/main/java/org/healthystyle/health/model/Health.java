package org.healthystyle.health.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.healthystyle.health.model.diet.Diet;
import org.healthystyle.health.model.medicine.Treatment;
import org.healthystyle.health.model.sport.Sport;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(indexes = { @Index(name = "health_user_id_idx", columnList = "userId", unique = true) })
public class Health {
	@Id
	@SequenceGenerator(name = "health_generator", sequenceName = "health_sequence", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "health_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(name = "user_id", nullable = false, unique = true)
	private Long userId;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "health")
	private List<Diet> diets;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "health")
	private List<Treatment> treatments;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "health")
	private List<Sport> sports;

	public Health(Long userId) {
		super();

		Objects.requireNonNull(userId, "User id must be not null");

		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public List<Diet> getDiets() {
		if (diets == null) {
			diets = new ArrayList<>();
		}
		return diets;
	}

	public void addDiet(Diet diet) {
		getDiets().add(diet);
	}

	public List<Treatment> getTreatments() {
		if (treatments == null) {
			treatments = new ArrayList<>();
		}
		return treatments;
	}

	public void addTreatment(Treatment treatment) {
		getTreatments().add(treatment);
	}

	public List<Sport> getSports() {
		if (sports == null) {
			sports = new ArrayList<>();
		}
		return sports;
	}

	public void addSport(Sport sport) {
		getSports().add(sport);
	}

}
