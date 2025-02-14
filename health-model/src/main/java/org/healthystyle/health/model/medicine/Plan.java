package org.healthystyle.health.model.medicine;

import java.time.Instant;
import java.time.LocalTime;

import org.healthystyle.health.model.diet.Meal;

public class Plan {
	private Long id;
	private Medicine medicine;
	private String weight;
	private Sequence sequence;
	private Instant start;
	private Instant end;
}
