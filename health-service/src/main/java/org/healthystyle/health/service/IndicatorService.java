package org.healthystyle.health.service;

import java.time.Instant;

import org.healthystyle.health.model.Indicator;
import org.healthystyle.health.repository.result.AvgStatistic;
import org.healthystyle.health.service.dto.IndicatorSaveRequest;
import org.healthystyle.health.service.dto.IndicatorUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.indicator.IndicatorNotFoundException;
import org.healthystyle.health.service.error.indicator.IndicatorTypeNotFoundException;
import org.healthystyle.health.service.error.indicator.NotConvertableException;
import org.healthystyle.health.service.error.indicator.NotOwnerException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

public interface IndicatorService {
	Indicator findById(Long id) throws IndicatorNotFoundException;
	
	Page<Indicator> findByIndicatorType(Long indicatorTypeId, int page, int limit)
			throws ValidationException;

	Page<Indicator> find(int page, int limit) throws ValidationException;

	Page<Indicator> findChangesByIndicatorType(Long indicatorTypeId, Instant from, Instant to, int page,
			int limit) throws ValidationException;

	Page<AvgStatistic> findChangesByIndicatorTypeWeeksRange(Long indicatorTypeId, Instant from,
			Instant to, int page, int limit) throws ValidationException;

	Page<AvgStatistic> findChangesByIndicatorTypeYearsRange(Long indicatorTypeId, Instant from,
			Instant to, int page, int limit) throws ValidationException;

	@Query("SELECT new org.healthystyle.health.repository.result.AvgStatistic(DATE_TRUNC('month' FROM i.createdOn) AS month, TO_CHAR(AVG(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(i.value AS INTEGER) WHEN TYPE(ct) = FloatNumber THEN CAST(i.value AS FLOAT) END), 'FM999999990.09')) FROM Indicator i INNER JOIN i.indicatorType it INNER JOIN it.convertType ct WHERE it.id = :indicatorTypeId AND i.createdOn >= :from AND i.createdOn <= :to) GROUP BY DATE_TRUNC('month' FROM i.createdOn) ORDER BY month")
	Page<AvgStatistic> findChangedByIndicatorTypeMonthsRange(Long indicatorTypeId, Instant from,
			Instant to, int page, int limit) throws ValidationException;

	Indicator save(IndicatorSaveRequest saveRequest) throws ValidationException, IndicatorTypeNotFoundException, NotConvertableException;

	void deleteById(Long id) throws NotOwnerException;

	void update(IndicatorUpdateRequest updateRequest) throws ValidationException, IndicatorNotFoundException, NotConvertableException;

}
