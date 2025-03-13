package org.healthystyle.health.service.medicine;

import org.healthystyle.health.model.medicine.Plan;

public interface PlanService {
	Plan findById(Long planId);
}
