package org.healthystyle.health.repository;

import java.time.Instant;
import java.util.List;

import org.healthystyle.health.model.Indicator;
import org.healthystyle.health.repository.result.AvgStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
	@Query("SELECT i FROM Indicator i INNER JOIN IndicatorType it ON i.indicatorType.id = it.id INNER JOIN Health h WHERE it.id = :indicatorTypeId AND h.id = :healthId ORDER BY i.createdOn DESC")
	Page<Indicator> findByIndicatorType(Long indicatorTypeId, Long healthId, Pageable pageable);

	@Query("SELECT i FROM Indicator i INNER JOIN Health h WHERE h.id = :healthId ORDER BY i.createdOn DESC")
	Page<Indicator> find(Long healthId, Pageable pageable);

	@Query("SELECT i FROM Indicator i INNER JOIN i.indicatorType it WHERE it.id = :indicatorTypeId AND i.createdOn >= :from AND i.createdOn <= :to ORDER BY i.createdOn")
	Page<Indicator> findChangesByIndicatorType(Long indicatorTypeId, Long healthId, Instant from, Instant to,
			Pageable pageable);

	@Query("SELECT new org.healthystyle.health.repository.result.AvgStatistic(DATE_TRUNC('week' FROM i.createdOn) AS weekly, TO_CHAR(AVG(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(i.value AS INTEGER) WHEN TYPE(ct) = FloatNumber THEN CAST(i.value AS FLOAT) END), 'FM999999990.09')) FROM Indicator i INNER JOIN i.indicatorType it INNER JOIN it.convertType ct WHERE it.id = :indicatorTypeId AND i.createdOn >= :from AND i.createdOn <= :to GROUP BY DATE_TRUNC('week' FROM i.createdOn) ORDER BY weekly")
	Page<AvgStatistic> findChangesByIndicatorTypeWeeksRange(Long indicatorTypeId, Long healthId, Instant from,
			Instant to, Pageable pageable);

	@Query("SELECT new org.healthystyle.health.repository.result.AvgStatistic(DATE_TRUNC('year' FROM i.createdOn) AS year, TO_CHAR(AVG(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(i.value AS INTEGER) WHEN TYPE(ct) = FloatNumber THEN CAST(i.value AS FLOAT) END), 'FM999999990.09')) FROM Indicator i INNER JOIN i.indicatorType it INNER JOIN it.convertType ct WHERE it.id = :indicatorTypeId AND i.createdOn >= :from AND i.createdOn <= :to GROUP BY DATE_TRUNC('year' FROM i.createdOn) ORDER BY year")
	Page<AvgStatistic> findChangesByIndicatorTypeYearsRange(Long indicatorTypeId, Long healthId, Instant from,
			Instant to, Pageable pageable);

	@Query("SELECT new org.healthystyle.health.repository.result.AvgStatistic(DATE_TRUNC('month' FROM i.createdOn) AS month, TO_CHAR(AVG(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(i.value AS INTEGER) WHEN TYPE(ct) = FloatNumber THEN CAST(i.value AS FLOAT) END), 'FM999999990.09')) FROM Indicator i INNER JOIN i.indicatorType it INNER JOIN it.convertType ct WHERE it.id = :indicatorTypeId AND i.createdOn >= :from AND i.createdOn <= :to) GROUP BY DATE_TRUNC('month' FROM i.createdOn) ORDER BY month")
	Page<AvgStatistic> findChangesByIndicatorTypeMonthsRange(Long indicatorTypeId, Long healthId, Instant from,
			Instant to, Pageable pageable);

	@Query("SELECT EXISTS (SELECT i FROM Indicator i INNER JOIN i.health h WHERE i.id = :id AND h.id = :healthId)")
	boolean existsByIdAndHealth(Long id, Long healthId);
}
