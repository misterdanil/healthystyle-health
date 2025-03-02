package org.healthystyle.health.model.sport;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(indexes = @Index(name = "train_day_idx", columnList = "day"))
public class Train {
	@Id
	@SequenceGenerator(name = "train_generator", sequenceName = "train_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "train_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(length = 1000)
	private String description;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "train")
	private List<Set> sets;
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	private Instant time;
	@Column(nullable = false)
	private Integer day;
	private Sport sport;

	public Train() {
		super();
	}

	public Train(Instant time, Integer day, Set... sets) {
		super();

		Objects.requireNonNull(time, "Time must be not null");
		Objects.requireNonNull(day, "Day must be not null");
		Objects.requireNonNull(sets, "Sets must be not null");
		if (sets.length == 0) {
			throw new IllegalArgumentException("Must be passed at least one set");
		}

		this.sets = new ArrayList<>(Arrays.asList(sets));
		this.time = time;
		this.day = day;
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

	public List<Set> getSets() {
		if (sets == null) {
			sets = new ArrayList<>();
		}
		return sets;
	}

	public void addSets(Set... sets) {
		getSets().addAll(Arrays.asList(sets));
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

}
