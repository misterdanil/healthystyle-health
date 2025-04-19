package org.healthystyle.health.service.sport;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.healthystyle.health.model.sport.Train;
import org.healthystyle.health.service.dto.sport.TrainSaveRequest;
import org.healthystyle.health.service.dto.sport.TrainUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.error.sport.SportNotFoundException;
import org.healthystyle.health.service.error.sport.TrainExistException;
import org.healthystyle.health.service.error.sport.TrainNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface TrainService {
	static final String[] FIND_BY_DESCRIPTION_PARAM_NAMES = MethodNameHelper.getMethodParamNames(TrainService.class,
			"findByDescription", String.class, int.class, int.class);
	static final String[] FIND_BY_DAY_PARAM_NAMES = MethodNameHelper.getMethodParamNames(TrainService.class,
			"findByDay", Integer.class, int.class, int.class);
	static final String[] FIND_BY_DATE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(TrainService.class,
			"findByDate", Instant.class, int.class, int.class);
	static final String[] FIND_BY_SPORT_PARAM_NAMES = MethodNameHelper.getMethodParamNames(TrainService.class,
			"findBySport", Long.class, int.class, int.class);
	static final String[] FIND_PLANNED_PARAM_NAMES = MethodNameHelper.getMethodParamNames(TrainService.class,
			"findPlanned", int.class, int.class);

	Train findById(Long id) throws ValidationException, TrainNotFoundException;

	Page<Train> findByDescription(String description, int page, int limit) throws ValidationException;

	Page<Train> findByDay(Integer day, int page, int limit) throws ValidationException;

	Page<Train> findByDate(Instant date, int page, int limit) throws ValidationException;

	Page<Train> findBySport(Long sportId, int page, int limit) throws ValidationException;

	Set<Train> findPlanned(int page, int limit) throws ValidationException;

	List<Train> findNextTrain();

	Train save(TrainSaveRequest saveRequest, Long sportId)
			throws ValidationException, TrainExistException, TrainNotFoundException, ExerciseNotFoundException, SportNotFoundException;

	void update(TrainUpdateRequest updateRequest, Long id)
			throws ValidationException, TrainExistException, TrainNotFoundException;
}
