package org.healthystyle.health.service.sport;

import org.healthystyle.health.model.sport.Step;
import org.healthystyle.health.service.dto.sport.StepSaveRequest;
import org.healthystyle.health.service.dto.sport.StepUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.error.sport.StepNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface StepService {
	static final String[] FIND_BY_EXERCISE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ExerciseService.class,
			"findByExercise", Long.class, int.class, int.class);

	Step findById(Long id) throws ValidationException, StepNotFoundException;

	Page<Step> findByExercise(Long id, int page, int limit) throws ValidationException;

	Step save(StepSaveRequest saveRequest, Long exerciseId) throws ValidationException, ExerciseNotFoundException;

	void update(StepUpdateRequest updateRequest, Long id) throws ValidationException, StepNotFoundException;
}
