package org.healthystyle.health.service.sport;

import org.healthystyle.health.model.sport.Sport;
import org.healthystyle.health.service.dto.sport.SportSaveRequest;
import org.healthystyle.health.service.dto.sport.SportUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.error.sport.SportNotFoundException;
import org.healthystyle.health.service.error.sport.TrainExistException;
import org.healthystyle.health.service.error.sport.TrainNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface SportService {
	static final String[] FIND_BY_DESCRIPTION_PARAM_NAMES = MethodNameHelper.getMethodParamNames(SportService.class,
			"findByDescription", String.class, int.class, int.class);
	static final String[] FIND_ACTUAL_PARAM_NAMES = MethodNameHelper.getMethodParamNames(SportService.class,
			"findActual", int.class, int.class);

	Sport findById(Long id) throws ValidationException, SportNotFoundException;

	Page<Sport> findByDescription(String description, int page, int limit) throws ValidationException;

	Page<Sport> findActual(int page, int limit) throws ValidationException;

	Sport save(SportSaveRequest saveRequest) throws ValidationException, TrainExistException, TrainNotFoundException,
			ExerciseNotFoundException, SportNotFoundException;

	void update(SportUpdateRequest updateRequest, Long id) throws ValidationException, SportNotFoundException;

}
