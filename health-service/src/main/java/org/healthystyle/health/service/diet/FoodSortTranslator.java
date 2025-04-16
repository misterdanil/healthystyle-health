package org.healthystyle.health.service.diet;

import org.healthystyle.health.service.dto.diet.FoodSort;
import org.springframework.stereotype.Component;

@Component
public class FoodSortTranslator {
	public String translateToSort(FoodSort sort) {
		if (sort.equals(FoodSort.TITLE)) {
			return "title";
		} else if (sort.equals(FoodSort.CREATED_ON)) {
			return "createdOn";
		} else {
			return null;
		}
	}
}
