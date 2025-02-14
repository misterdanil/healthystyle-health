package org.healthystyle.health.model.medicine;

import java.time.Instant;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

public class Result {
	private Long id;
	private Intake intake;
	private Instant createdOn;
}
