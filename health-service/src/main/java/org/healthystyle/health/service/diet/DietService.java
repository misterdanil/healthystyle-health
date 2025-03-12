package org.healthystyle.health.service.diet;

import java.time.Instant;

import org.healthystyle.health.model.diet.Diet;
import org.healthystyle.health.service.dto.diet.DietSaveRequest;
import org.healthystyle.health.service.dto.diet.DietUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.DietExistException;
import org.healthystyle.health.service.error.diet.DietNotFoundException;
import org.healthystyle.health.service.error.diet.MealFoodNotFoundException;
import org.healthystyle.health.service.error.diet.MealSaveException;
import org.healthystyle.health.service.error.diet.MealTimeDuplicateException;
import org.springframework.data.domain.Page;

public interface DietService {
	Diet findById(Long id) throws DietNotFoundException, ValidationException;

	Page<Diet> findByTitle(String title, int page, int limit) throws ValidationException;

	Page<Diet> findByStart(Instant start, int page, int limit) throws ValidationException;

	Page<Diet> findActual(int page, int limit) throws ValidationException;

	Page<Diet> findByDate(Instant date, int page, int limit) throws ValidationException;

	Diet save(DietSaveRequest saveRequest) throws ValidationException, DietExistException, MealTimeDuplicateException,
			MealFoodNotFoundException, MealSaveException;

	void update(DietUpdateRequest updateRequest, Long dietId)
			throws ValidationException, DietNotFoundException, DietExistException;

	void deleteById(Long id) throws ValidationException, DietNotFoundException;
}
