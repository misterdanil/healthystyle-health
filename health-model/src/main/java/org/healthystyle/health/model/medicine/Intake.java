package org.healthystyle.health.model.medicine;

import java.time.LocalTime;
import java.util.Objects;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.convert.ConvertType;

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
	private LocalTime time;
	@Column(nullable = false, columnDefinition = "INTEGER CONSTRAINT intake_day_check CHECK (day >= 0 AND day <= 6)")
	private Integer day;
	private String weight;
	@ManyToOne
	@JoinColumn(name = "measure_id")
	private Measure measure;
	@ManyToOne
	@JoinColumn(name = "convert_type_id")
	private ConvertType convertType;

	public Intake() {
		super();
	}

	public Intake(Plan plan, LocalTime time, Integer day) {
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

	public void setId(Long id) {
		this.id = id;
	}

	public Plan getPlan() {
		return plan;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
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

}
