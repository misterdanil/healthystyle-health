package org.healthystyle.health.service.sport;

import java.time.Instant;
import java.time.LocalDate;

import org.healthystyle.health.model.sport.Result;
import org.healthystyle.health.repository.result.DateStatistic;
import org.healthystyle.health.repository.result.TimeStatistic;
import org.healthystyle.health.service.dto.sport.ResultSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ResultExistException;
import org.healthystyle.health.service.error.sport.ResultNotFoundException;
import org.healthystyle.health.service.error.sport.SetNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface ResultService {
	static final String[] FIND_BY_SET_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ResultService.class,
			"findBySet", Long.class, int.class, int.class);
	static final String[] FIND_BY_TRAIN_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ResultService.class,
			"findByTrain", Long.class, int.class, int.class);
	static final String[] FIND_BY_DATE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ResultService.class,
			"findByDate", Instant.class, int.class, int.class);
	static final String[] FIND_BY_SPORT_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ResultService.class,
			"findBySport", Long.class, int.class, int.class);
	static final String[] FIND_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ResultService.class, "find",
			int.class, int.class);
	static final String[] FIND_PERCENTAGE_BY_SPORT_PARAM_NAMES = MethodNameHelper
			.getMethodParamNames(ResultService.class, "findPercentageBySport", Long.class, int.class, int.class);
	static final String[] FIND_PERCENTAGE_BY_TRAIN_PARAM_NAMES = MethodNameHelper
			.getMethodParamNames(ResultService.class, "findPercentageByTrain", Long.class, int.class, int.class);
	static final String[] FIND_PERCENTAGE_BY_DATE_PARAM_NAMES = MethodNameHelper
			.getMethodParamNames(ResultService.class, "findPercentageByDate", LocalDate.class, int.class, int.class);
	static final String[] FIND_PERCENTAGE_RANGE_WEEKS_PARAM_NAMES = MethodNameHelper.getMethodParamNames(
			ResultService.class, "findPercentageRangeWeeks", LocalDate.class, LocalDate.class, int.class, int.class);
	static final String[] FIND_PERCENTAGE_RANGE_MONTHS_PARAM_NAMES = MethodNameHelper.getMethodParamNames(
			ResultService.class, "findPercentageRangeMonths", LocalDate.class, LocalDate.class, int.class, int.class);

	Result findById(Long id) throws ValidationException, ResultNotFoundException;

	Page<Result> findBySet(Long setId, int page, int limit) throws ValidationException;

	Page<Result> findByTrain(Long trainId, int page, int limit) throws ValidationException;

	Page<Result> findByDate(Instant date, int page, int limit) throws ValidationException;

	Page<Result> findBySport(Long sportId, int page, int limit) throws ValidationException;

	Page<Result> find(int page, int limit) throws ValidationException;

	Page<DateStatistic> findPercentageBySport(Long sportId, int page, int limit) throws ValidationException;

	Page<DateStatistic> findPercentageByTrain(Long trainId, int page, int limit) throws ValidationException;

	Page<TimeStatistic> findPercentageByDate(LocalDate date, int page, int limit) throws ValidationException;

	Page<DateStatistic> findPercentageRangeDays(LocalDate start, LocalDate end, int page, int limit)
			throws ValidationException;

	Page<DateStatistic> findPercentageRangeWeeks(LocalDate start, LocalDate end, int page, int limit)
			throws ValidationException;

	Page<DateStatistic> findPercentageRangeMonths(LocalDate start, LocalDate end, int page, int limit)
			throws ValidationException;

	Result save(ResultSaveRequest saveRequest, Long setId)
			throws ValidationException, ResultExistException, SetNotFoundException;

	void deleteById(Long id) throws ValidationException, ResultNotFoundException;

}
