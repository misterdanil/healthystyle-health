package org.healthystyle.health.model.sport;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

public class Train {
	private Long id;
	private List<Set> sets;
	@Temporal(TemporalType.TIME)
	private Instant time;
}
