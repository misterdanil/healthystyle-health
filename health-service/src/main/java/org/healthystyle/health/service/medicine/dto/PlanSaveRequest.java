package org.healthystyle.health.service.medicine.dto;

import java.time.LocalDate;
import java.util.List;

import org.healthystyle.health.model.medicine.Sequence;
import org.healthystyle.health.service.validation.annotation.IsAfter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@IsAfter(beforeField = "start", afterField = "end", message = "Дата начала приёма лекарства должна быть раньше даты конца")
public class PlanSaveRequest {
	@NotNull(message = "Укажите идентификатор лекарства для добавления в план")
	private Long medicineId;
	@NotEmpty(message = "Укажите хотя бы один приём лекарства")
	private List<IntakeSaveRequest> intakes;
//	private String weight;
//	private Type measure;
	private Sequence sequence;
	@NotNull(message = "Укажите дату начала приёма лекарства")
	private LocalDate start;
	@NotNull(message = "Укажите дату конца приёма лекарства")
	private LocalDate end;

	public Long getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(Long medicineId) {
		this.medicineId = medicineId;
	}

	public List<IntakeSaveRequest> getIntakes() {
		return intakes;
	}

	public void setIntakes(List<IntakeSaveRequest> intakes) {
		this.intakes = intakes;
	}

//	public String getWeight() {
//		return weight;
//	}
//
//	public void setWeight(String weight) {
//		this.weight = weight;
//	}
//
//	public Type getMeasure() {
//		return measure;
//	}
//
//	public void setMeasure(Type measure) {
//		this.measure = measure;
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

}
