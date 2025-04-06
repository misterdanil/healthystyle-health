package org.healthystyle.health.service;

import java.time.LocalDateTime;

import org.healthystyle.health.model.Indicator;
import org.healthystyle.health.repository.result.AvgStatistic;
import org.healthystyle.health.service.dto.IndicatorSaveRequest;
import org.healthystyle.health.service.dto.IndicatorSort;
import org.healthystyle.health.service.dto.IndicatorUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.indicator.IndicatorNotFoundException;
import org.healthystyle.health.service.error.indicator.IndicatorTypeNotFoundException;
import org.healthystyle.health.service.error.indicator.NotConvertableException;
import org.healthystyle.health.service.error.indicator.NotOwnerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

public interface IndicatorService {
	Indicator findById(Long id) throws IndicatorNotFoundException;

	Page<Indicator> findByIndicatorType(Long indicatorTypeId, int page, int limit, IndicatorSort sort,
			Direction direction) throws ValidationException;

	Page<Indicator> find(int page, int limit, IndicatorSort sort, Direction direction) throws ValidationException;

	Page<Indicator> findChangesByIndicatorType(Long indicatorTypeId, LocalDateTime from, LocalDateTime to, int page, int limit)
			throws ValidationException;

	Page<AvgStatistic> findChangesByIndicatorTypeWeeksRange(Long indicatorTypeId, LocalDateTime from, LocalDateTime to,
			int page, int limit) throws ValidationException;

	Page<AvgStatistic> findChangesByIndicatorTypeYearsRange(Long indicatorTypeId, LocalDateTime from, LocalDateTime to,
			int page, int limit) throws ValidationException;

	Page<AvgStatistic> findChangedByIndicatorTypeMonthsRange(Long indicatorTypeId, LocalDateTime from, LocalDateTime to,
			int page, int limit) throws ValidationException;

	Indicator save(IndicatorSaveRequest saveRequest)
			throws ValidationException, IndicatorTypeNotFoundException, NotConvertableException;

	void deleteById(Long id) throws NotOwnerException;

	void update(IndicatorUpdateRequest updateRequest)
			throws ValidationException, IndicatorNotFoundException, NotConvertableException;

}
