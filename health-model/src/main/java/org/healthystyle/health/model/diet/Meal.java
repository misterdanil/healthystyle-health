package org.healthystyle.health.model.diet;

import java.time.LocalTime;
import java.util.List;

import org.healthystyle.health.model.medicine.Intake;

public class Meal {
	private Long id;
	private List<Food> foods;
	private LocalTime time;
}
