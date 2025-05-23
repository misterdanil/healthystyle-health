package org.healthystyle.health.service.dto.sport;

import java.time.LocalDate;
import java.util.List;

import org.healthystyle.health.service.validation.annotation.IsAfter;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@IsAfter(beforeField = "start", afterField = "end", message = "Начало должно быть раньше конца")
public class SportSaveRequest {
	@NotBlank(message = "Укажите описание")
	private String description;
	@NotNull(message = "Укажите начало")
//	@FutureOrPresent(message = "Дата начала должна быть в будущем или настоящем")
	private LocalDate start;
	@NotNull(message = "Укажите конец")
	@FutureOrPresent(message = "Дата конца должна быть в будущем или настоящем")
	private LocalDate end;
	@NotEmpty(message = "Укажите тренировки")
	private List<TrainSaveRequest> trains;

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

	public List<TrainSaveRequest> getTrains() {
		return trains;
	}

	public void setTrains(List<TrainSaveRequest> trains) {
		this.trains = trains;
	}

	@Override
	public String toString() {
		return "SportSaveRequest [description=" + description + ", start=" + start + ", end=" + end + ", trains="
				+ trains + "]";
	}

}
