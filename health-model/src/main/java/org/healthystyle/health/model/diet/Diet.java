package org.healthystyle.health.model.diet;

import java.time.Instant;
import java.util.List;

import org.healthystyle.health.model.Health;

public class Diet {
	private Long id;
	private String description;
	private List<Schedule> schedules;
	private Instant start;
	private Instant end;
	private Health health;

}
