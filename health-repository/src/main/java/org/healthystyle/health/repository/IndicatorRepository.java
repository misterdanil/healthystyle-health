package org.healthystyle.health.repository;

import java.time.LocalDateTime;

import org.healthystyle.health.model.Indicator;
import org.healthystyle.health.repository.result.AvgStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
	@Query("SELECT i FROM Indicator i INNER JOIN IndicatorType it ON i.indicatorType.id = it.id WHERE it.id = :indicatorTypeId AND i.health.id = :healthId")
	Page<Indicator> findByIndicatorType(Long indicatorTypeId, Long healthId, Pageable pageable);

	@Query("SELECT i FROM Indicator i WHERE i.health.id = :healthId")
	Page<Indicator> find(Long healthId, Pageable pageable);

	@Query("SELECT i FROM Indicator i INNER JOIN i.indicatorType it WHERE it.id = :indicatorTypeId AND i.createdOn >= :from AND i.createdOn <= :to ORDER BY i.createdOn")
	Page<Indicator> findChangesByIndicatorType(Long indicatorTypeId, Long healthId, LocalDateTime from, LocalDateTime to,
			Pageable pageable);

	@Query("SELECT new org.healthystyle.health.repository.result.AvgStatistic(DATE_TRUNC('week', i.createdOn) AS weekly, TO_CHAR(AVG(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(i.value AS INTEGER) WHEN TYPE(ct) = FloatNumber THEN CAST(i.value AS FLOAT) END), 'FM999999990.09')) FROM Indicator i INNER JOIN i.indicatorType it INNER JOIN it.convertType ct WHERE it.id = :indicatorTypeId AND i.createdOn >= :from AND i.createdOn <= :to AND i.health.id = :healthId GROUP BY DATE_TRUNC('week', i.createdOn) ORDER BY weekly")
	Page<AvgStatistic> findChangesByIndicatorTypeWeeksRange(Long indicatorTypeId, Long healthId, LocalDateTime from,
			LocalDateTime to, Pageable pageable);

	@Query("SELECT new org.healthystyle.health.repository.result.AvgStatistic(DATE_TRUNC('year', i.createdOn) AS year, TO_CHAR(AVG(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(i.value AS INTEGER) WHEN TYPE(ct) = FloatNumber THEN CAST(i.value AS FLOAT) END), 'FM999999990.09')) FROM Indicator i INNER JOIN i.indicatorType it INNER JOIN it.convertType ct WHERE it.id = :indicatorTypeId AND i.createdOn >= :from AND i.createdOn <= :to AND i.health.id = :healthId GROUP BY DATE_TRUNC('year', i.createdOn) ORDER BY year")
	Page<AvgStatistic> findChangesByIndicatorTypeYearsRange(Long indicatorTypeId, Long healthId, LocalDateTime from,
			LocalDateTime to, Pageable pageable);

	@Query("SELECT new org.healthystyle.health.repository.result.AvgStatistic(DATE_TRUNC('month', i.createdOn) AS month, TO_CHAR(AVG(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(i.value AS INTEGER) WHEN TYPE(ct) = FloatNumber THEN CAST(i.value AS FLOAT) END), 'FM999999990.09')) FROM Indicator i INNER JOIN i.indicatorType it INNER JOIN it.convertType ct WHERE it.id = :indicatorTypeId AND i.createdOn >= :from AND i.createdOn <= :to AND i.health.id = :healthId GROUP BY DATE_TRUNC('month', i.createdOn) ORDER BY month")
	Page<AvgStatistic> findChangesByIndicatorTypeMonthsRange(Long indicatorTypeId, Long healthId, LocalDateTime from,
			LocalDateTime to, Pageable pageable);

	@Query("SELECT EXISTS (SELECT i FROM Indicator i INNER JOIN i.health h WHERE i.id = :id AND h.id = :healthId)")
	boolean existsByIdAndHealth(Long id, Long healthId);
}
