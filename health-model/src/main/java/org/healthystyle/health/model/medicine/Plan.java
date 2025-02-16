package org.healthystyle.health.model.medicine;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(indexes = @Index(name = "plan_start_idx", columnList = "start"))
public class Plan {
	@Id
	@SequenceGenerator(name = "plan_generator", sequenceName = "plan_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "plan_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "medicine_id", nullable = false)
	private Medicine medicine;
	private String weight;
	@Enumerated(EnumType.STRING)
	private Sequence sequence;
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Instant start;
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Instant end;
	@ManyToOne
	@JoinColumn(name = "treatment_id", nullable = false)
	private Treatment treatment;

	public Plan() {
		super();
	}

	public Plan(Medicine medicine, Instant start, Treatment treatment) {
		super();

		Objects.requireNonNull(medicine, "Medicine must be not null");
		Objects.requireNonNull(start, "Start must be not null");
		Objects.requireNonNull(treatment, "Treatment must be not null");

		this.medicine = medicine;
		this.start = start;
		this.treatment = treatment;
	}

	public Long getId() {
		return id;
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	public Instant getStart() {
		return start;
	}

	public void setStart(Instant start) {
		this.start = start;
	}

	public Instant getEnd() {
		return end;
	}

	public void setEnd(Instant end) {
		this.end = end;
	}

	public Treatment getTreatment() {
		return treatment;
	}
}
