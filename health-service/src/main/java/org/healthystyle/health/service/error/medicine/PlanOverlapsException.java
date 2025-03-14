package org.healthystyle.health.service.error.medicine;

import java.time.Instant;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class PlanOverlapsException extends AbstractException {
	private Instant start;
	private Instant end;
	private Long medicineId;

	public PlanOverlapsException(Instant start, Instant end, Long medicineId, BindingResult result) {
		super(result, "There is already plan with medicine intakes '%s' from start '%s' to end '%s'", medicineId, start,
				end);
		this.start = start;
		this.end = end;
		this.medicineId = medicineId;
	}

	public Instant getStart() {
		return start;
	}

	public Instant getEnd() {
		return end;
	}

	public Long getMedicineId() {
		return medicineId;
	}

}
