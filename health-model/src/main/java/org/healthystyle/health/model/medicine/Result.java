package org.healthystyle.health.model.medicine;

import java.time.LocalDate;
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

@Entity(name = "MedicineResult")
@Table(name = "intake_result", indexes = @Index(name = "intake_result_intake_id_idx", columnList = "intake_id"))
public class Result {
	@Id
	@SequenceGenerator(name = "intake_result_generator", sequenceName = "intake_result_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "intake_result_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "intake_id", nullable = false)
	private Intake intake;
	@Column(name = "created_on", nullable = false)
	private LocalDate createdOn;

	public Result() {
		super();
	}

	public Result(Intake intake, LocalDate createdOn) {
		super();

		Objects.requireNonNull(intake, "Intake must be not null");

		this.intake = intake;
		this.createdOn = createdOn;
	}

	public Long getId() {
		return id;
	}

	public Intake getIntake() {
		return intake;
	}

	public LocalDate getCreatedOn() {
		return createdOn;
	}

}
