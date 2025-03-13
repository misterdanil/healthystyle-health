package org.healthystyle.health.service.diet;

import java.util.List;
import java.util.Set;

import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.service.dto.diet.FoodSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeMismatchException;
import org.healthystyle.health.service.error.diet.FoodExistException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodValueExistException;
import org.healthystyle.health.service.error.diet.NutritionValueNotFoundException;
import org.springframework.data.domain.Page;

public interface FoodService {
	Food findById(Long id) throws ValidationException, FoodNotFoundException;

	List<Food> findByIds(Set<Long> ids) throws ValidationException;

	Page<Food> find(int page, int limit) throws ValidationException;

	Page<Food> findByTitle(String title, int page, int limit) throws ValidationException;

	Food save(FoodSaveRequest saveRequest)
			throws ValidationException, FoodExistException, NutritionValueNotFoundException, FoodValueExistException,
			FoodNotFoundException, ConvertTypeMismatchException;

	void update(FoodUpdateRequest updateRequest, Long foodId)
			throws ValidationException, FoodNotFoundException, FoodExistException;

	void deleteById(Long id) throws FoodNotFoundException, ValidationException;
}
