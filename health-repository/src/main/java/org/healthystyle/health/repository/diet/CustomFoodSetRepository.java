package org.healthystyle.health.repository.diet;

import java.util.List;

import org.healthystyle.health.model.diet.Food;

public interface CustomFoodSetRepository {
	void addFoods(List<Food> foods, Long foodSetId);
}
