package org.healthystyle.health.service.diet;

import java.util.List;
import java.util.Set;

import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.service.dto.diet.FoodSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodUpdateRequest;
import org.healthystyle.health.service.dto.diet.FoodUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FoodService {
	Food findById(Long id) throws ValidationException, FoodNotFoundException;

	Page<Food> find(Long healthId, Pageable pageable) throws ValidationException;

	Page<Food> findByTitle(String title, Long healthId, Pageable pageable) throws ValidationException;

	List<Food> findByIdsExcludeMeal(Set<Long> ids, Long mealId) throws ValidationException;

	Food save(FoodSaveRequest saveRequest) throws ValidationException;

	void update(FoodUpdateRequest updateRequest) throws ValidationException;

	void deleteById(Long id) throws FoodNotFoundException;
}
