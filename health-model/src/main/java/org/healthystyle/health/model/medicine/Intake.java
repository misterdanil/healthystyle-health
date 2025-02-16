package org.healthystyle.health.model.medicine;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(indexes = { @Index(name = "intake_plan_id_idx", columnList = "plan_id"),
		@Index(name = "intake_day_idx", columnList = "day") })
public class Intake {
	@Id
	@SequenceGenerator(name = "intake_generator", sequenceName = "intake_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "intake_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "plan_id", nullable = false)
	private Plan plan;
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	private Instant time;
	@Column(nullable = false, columnDefinition = "INTEGER CONSTRAINT intake_day_check CHECK (day >= 0 AND day <= 6)")
	private Integer day;

	public Intake() {
		super();
	}

	public Intake(Plan plan, Instant time, Integer day) {
		super();

		Objects.requireNonNull(plan, "Plan must be not null");
		Objects.requireNonNull(time, "Time must be not null");
		Objects.requireNonNull(day, "Day must be not null");

		this.plan = plan;
		this.time = time;
		this.day = day;
	}

	public Long getId() {
		return id;
	}

	public Plan getPlan() {
		return plan;
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
