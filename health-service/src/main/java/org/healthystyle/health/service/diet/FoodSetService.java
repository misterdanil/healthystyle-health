package org.healthystyle.health.service.diet;

import java.util.List;

import org.healthystyle.health.model.diet.FoodSet;
import org.healthystyle.health.service.dto.diet.FoodSetSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodSetUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodSetExistException;
import org.healthystyle.health.service.error.diet.FoodSetNoFoodsException;
import org.healthystyle.health.service.error.diet.FoodSetNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface FoodSetService {
	static final String[] FIND_BY_TITLE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(FoodSetService.class,
			"findByTitle", String.class, int.class, int.class);

	static final String[] FIND_BY_FOODS_PARAM_NAMES = MethodNameHelper.getMethodParamNames(FoodSetService.class,
			"findByFoods", Long.class, int.class, int.class);

	static final String[] FIND_LAST_PARAM_NAMES = MethodNameHelper.getMethodParamNames(FoodSetService.class, "findLast",
			int.class, int.class);

	FoodSet findById(Long id) throws FoodSetNotFoundException, ValidationException;

	Page<FoodSet> findByTitle(String title, int page, int limit) throws ValidationException;

	Page<FoodSet> findByFoods(List<Long> foodIds, int page, int limit) throws ValidationException;

	Page<FoodSet> findLast(int page, int limt) throws ValidationException;

	FoodSet save(FoodSetSaveRequest saveRequest)
			throws ValidationException, FoodSetExistException, FoodNotFoundException;

	void update(FoodSetUpdateRequest updateRequest, Long foodSetId) throws ValidationException, FoodSetExistException,
			FoodNotFoundException, FoodSetNoFoodsException, FoodSetNotFoundException;

	void deleteById(Long id) throws ValidationException, FoodSetNotFoundException;

}
