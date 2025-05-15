package org.healthystyle.health.model.medicine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.healthystyle.health.model.Health;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(indexes = @Index(name = "plan_start_idx", columnList = "start"))
public class Plan {
	@Id
	@SequenceGenerator(name = "plan_generator", sequenceName = "plan_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "plan_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "medicine_id", nullable = false)
	private Medicine medicine;
	@OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Intake> intakes;
//	private String weight;
//	private Measure measure;
//	private ConvertType convertType;
	@Enumerated(EnumType.STRING)
	private Sequence sequence;
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private LocalDate start;
	@Temporal(TemporalType.DATE)
	@Column(name = "finish", nullable = false)
	private LocalDate end;
	@ManyToOne
	@JoinColumn(name = "treatment_id", nullable = false)
	private Treatment treatment;
	@ManyToOne
	@JoinColumn(name = "health_id", nullable = false)
	private Health health;

	public Plan() {
		super();
	}

	public Plan(Medicine medicine, LocalDate start, LocalDate end, Treatment treatment, Health health) {
		super();

		Objects.requireNonNull(medicine, "Medicine must be not null");
		Objects.requireNonNull(start, "Start must be not null");
		Objects.requireNonNull(end, "End must be not null");
		Objects.requireNonNull(treatment, "Treatment must be not null");
		Objects.requireNonNull(health, "Health must be not null");

		this.medicine = medicine;
		this.start = start;
		this.end = end;
		this.treatment = treatment;
		this.health = health;
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

	public List<Intake> getIntakes() {
		if (intakes == null) {
			intakes = new ArrayList<>();
		}
		return intakes;
	}

	public void addIntake(Intake intake) {
		getIntakes().add(intake);
	}

	public void addIntakes(List<Intake> intakes) {
		getIntakes().addAll(intakes);
	}

//	public String getWeight() {
//		return weight;
//	}
//
//	public void setWeight(String weight) {
//		this.weight = weight;
//	}
//
//	public Measure getMeasure() {
//		return measure;
//	}
//
//	public void setMeasure(Measure measure) {
//		this.measure = measure;
//	}
//
//	public ConvertType getConvertType() {
//		return convertType;
//	}
//
//	public void setConvertType(ConvertType convertType) {
//		this.convertType = convertType;
//	}

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public Treatment getTreatment() {
		return treatment;
	}

	public Health getHealth() {
		return health;
	}

}
