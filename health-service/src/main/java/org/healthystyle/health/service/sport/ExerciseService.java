package org.healthystyle.health.service.sport;

import org.healthystyle.health.model.sport.Exercise;
import org.healthystyle.health.service.dto.sport.ExerciseSaveRequest;
import org.healthystyle.health.service.dto.sport.ExerciseSort;
import org.healthystyle.health.service.dto.sport.ExerciseUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseExistException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface ExerciseService {
	static final String[] FIND_BY_TITLE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ExerciseService.class,
			"findByTitle", String.class, int.class, int.class, ExerciseSort.class);
	static final String[] FIND_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ExerciseService.class, "find",
			int.class, int.class);

	Exercise findById(Long id) throws ValidationException, ExerciseNotFoundException;

	Page<Exercise> findByTitle(String title, int page, int limit, ExerciseSort sort) throws ValidationException;

	Page<Exercise> find(int page, int limit) throws ValidationException;

	Exercise save(ExerciseSaveRequest saveRequest)
			throws ValidationException, ExerciseExistException, ExerciseNotFoundException;

	boolean existsById(Long id) throws ValidationException;

	void update(ExerciseUpdateRequest updateRequest, Long id)
			throws ValidationException, ExerciseNotFoundException, ExerciseExistException;

}
