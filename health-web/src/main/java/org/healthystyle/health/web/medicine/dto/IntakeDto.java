package org.healthystyle.health.web.medicine.dto;

import java.time.LocalTime;

import org.healthystyle.health.web.measure.dto.MeasureDto;

public class IntakeDto {
	private Long id;
	private PlanDto plan;
	private LocalTime time;
	private Integer day;
	private String weight;
	private MeasureDto measure;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PlanDto getPlan() {
		return plan;
	}

	public void setPlan(PlanDto plan) {
		this.plan = plan;
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

	public MeasureDto getMeasure() {
		return measure;
	}

	public void setMeasure(MeasureDto measure) {
		this.measure = measure;
	}

}
