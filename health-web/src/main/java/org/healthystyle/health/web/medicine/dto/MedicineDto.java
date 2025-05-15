package org.healthystyle.health.web.medicine.dto;

import java.time.Instant;

import org.healthystyle.health.web.measure.dto.MeasureDto;

public class MedicineDto {
	private Long id;
	private String name;
	private String weight;
	private MeasureDto measure;
	private Instant createdOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Instant getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Instant createdOn) {
		this.createdOn = createdOn;
	}

}
