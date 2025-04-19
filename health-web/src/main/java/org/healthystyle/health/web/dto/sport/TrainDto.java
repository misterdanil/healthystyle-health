package org.healthystyle.health.web.dto.sport;

import java.time.LocalTime;
import java.util.List;

public class TrainDto {
	private Long id;
	private String description;
	private Integer day;
	private LocalTime time;
	private List<SetDto> sets;
	private Long sportId;
	private String sportDescription;

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

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public List<SetDto> getSets() {
		return sets;
	}

	public void setSets(List<SetDto> sets) {
		this.sets = sets;
	}

	public Long getSportId() {
		return sportId;
	}

	public void setSportId(Long sportId) {
		this.sportId = sportId;
	}

	public String getSportDescription() {
		return sportDescription;
	}

	public void setSportDescription(String sportDescription) {
		this.sportDescription = sportDescription;
	}
	
}
