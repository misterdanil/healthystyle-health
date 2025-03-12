package org.healthystyle.health.service.diet;

import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.service.dto.diet.FoodSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.springframework.data.domain.Page;

public interface FoodService {
	Food findById(Long id) throws ValidationException, FoodNotFoundException;

	Page<Food> find(int page, int limit) throws ValidationException;

	Page<Food> findByTitle(String title, int page, int limit) throws ValidationException;

	Food save(FoodSaveRequest saveRequest) throws ValidationException;

	void update(FoodUpdateRequest updateRequest) throws ValidationException;

	void deleteById(Long id) throws FoodNotFoundException;
}
