package org.healthystyle.health.model.medicine;

import java.util.List;

import org.healthystyle.health.model.Health;

public class Treatment {
	private Long id;
	private String description;
	private List<Plan> plans;
	private Health health;
}
