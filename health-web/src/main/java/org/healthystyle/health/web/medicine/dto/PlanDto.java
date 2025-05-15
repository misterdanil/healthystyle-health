package org.healthystyle.health.web.medicine.dto;

import java.time.LocalDate;
import java.util.List;

import org.healthystyle.health.model.medicine.Intake;
import org.healthystyle.health.model.medicine.Sequence;

public class PlanDto {
	private Long id;
	private MedicineDto medicine;
	private Sequence sequence;
	private LocalDate start;
	private LocalDate end;
	private TreatmentDto treatment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MedicineDto getMedicine() {
		return medicine;
	}

	public void setMedicine(MedicineDto medicine) {
		this.medicine = medicine;
	}

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

	public TreatmentDto getTreatment() {
		return treatment;
	}

	public void setTreatment(TreatmentDto treatment) {
		this.treatment = treatment;
	}

}
