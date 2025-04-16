package org.healthystyle.health.service.diet;

import java.util.Set;

import org.healthystyle.health.model.diet.MealFood;
import org.healthystyle.health.service.dto.diet.MealFoodSaveRequest;
import org.healthystyle.health.service.dto.diet.MealFoodUpdateRequest;
import org.healthystyle.health.service.error.AbstractException;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.MealFoodExistException;
import org.healthystyle.health.service.error.diet.MealNotFoundException;
import org.healthystyle.health.service.error.diet.NoFoodsException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

public interface MealFoodService {
	MealFood findById(Long id) throws ValidationException, MealFoodNotFoundException;

	Page<MealFood> findByMeal(Long mealId, int page, int limit) throws ValidationException;

	MealFood save(MealFoodSaveRequest saveRequest, Long mealId) throws ValidationException, MealNotFoundException,
			FoodNotFoundException, MealFoodExistException, MeasureNotFoundException, ConvertTypeNotRecognizedException;

	void update(MealFoodUpdateRequest updateRequest, Long id) throws ValidationException, MealFoodNotFoundException,
			MeasureNotFoundException, ConvertTypeNotRecognizedException;

	void deleteById(Long id) throws ValidationException, MealFoodNotFoundException;

	void deleteByIds(Set<Long> ids, Long mealId) throws ValidationException, NoFoodsException;

	public static class MealFoodNotFoundException extends AbstractException {
		private Long id;

		public MealFoodNotFoundException(Long id, BindingResult result) {
			super(result, "Could not find a meal food by id '%s'", id);
			this.id = id;
		}

		public Long getId() {
			return id;
		}

	}
}
