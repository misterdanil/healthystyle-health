package org.healthystyle.health.web.dto.sport;

import java.time.Instant;
import java.util.List;

public class ExerciseDto {
	private Long id;
	private String title;
	private List<StepDto> steps;
	private Instant createdOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<StepDto> getSteps() {
		return steps;
	}

	public void setSteps(List<StepDto> steps) {
		this.steps = steps;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Instant createdOn) {
		this.createdOn = createdOn;
	}

}
