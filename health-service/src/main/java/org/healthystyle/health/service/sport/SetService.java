package org.healthystyle.health.service.sport;

import org.healthystyle.health.model.sport.Set;
import org.healthystyle.health.service.dto.sport.SetSaveRequest;
import org.healthystyle.health.service.dto.sport.SetUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.error.sport.SetNotFoundException;
import org.healthystyle.health.service.error.sport.TrainNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface SetService {
	static final String[] FIND_BY_TRAIN_PARAM_NAMES = MethodNameHelper.getMethodParamNames(SetService.class,
			"findByTrain", Long.class, int.class, int.class);
	static final String[] FIND_BY_EXERCISE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(SetService.class,
			"findByExercise", Long.class, int.class, int.class);

	Set findById(Long id) throws ValidationException, SetNotFoundException;

	Page<Set> findByTrain(Long trainId, int page, int limit) throws ValidationException;

	Page<Set> findByExercise(Long exerciseId, int page, int limit) throws ValidationException;

	Set save(SetSaveRequest saveRequest, Long trainId)
			throws ValidationException, TrainNotFoundException, ExerciseNotFoundException;

	void update(SetUpdateRequest updateRequest, Long id) throws ValidationException, SetNotFoundException;

}
