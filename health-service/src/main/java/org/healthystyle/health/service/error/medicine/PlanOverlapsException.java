package org.healthystyle.health.service.error.medicine;

import java.time.LocalDate;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class PlanOverlapsException extends AbstractException {
	private LocalDate start;
	private LocalDate end;
	private Long medicineId;

	public PlanOverlapsException(LocalDate start, LocalDate end, Long medicineId, BindingResult result) {
		super(result, "There is already plan with medicine intakes '%s' from start '%s' to end '%s'", medicineId, start,
				end);
		this.start = start;
		this.end = end;
		this.medicineId = medicineId;
	}

	public LocalDate getStart() {
		return start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public Long getMedicineId() {
		return medicineId;
	}

}
