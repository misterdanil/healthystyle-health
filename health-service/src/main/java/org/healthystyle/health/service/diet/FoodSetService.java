package org.healthystyle.health.service.diet;

import org.healthystyle.health.model.diet.FoodSet;
import org.healthystyle.health.service.error.diet.FoodSetNotFoundException;

public interface FoodSetService {
	FoodSet findById(Long id) throws FoodSetNotFoundException;
}
