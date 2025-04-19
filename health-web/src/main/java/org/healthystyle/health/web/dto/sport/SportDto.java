package org.healthystyle.health.web.dto.sport;

import java.time.LocalDate;
import java.util.List;

public class SportDto {
	private Long id;
	private String description;
	private LocalDate start;
	private LocalDate end;
	private List<TrainDto> trains;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<TrainDto> getTrains() {
		return trains;
	}

	public void setTrains(List<TrainDto> trains) {
		this.trains = trains;
	}

}
