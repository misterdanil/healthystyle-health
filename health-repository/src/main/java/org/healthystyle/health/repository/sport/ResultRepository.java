package org.healthystyle.health.repository.sport;

import java.time.Instant;
import java.time.LocalDate;

import org.healthystyle.health.model.sport.Result;
import org.healthystyle.health.repository.result.DateStatistic;
import org.healthystyle.health.repository.result.TimeStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
	@Query("SELECT r FROM Result r INNER JOIN r.set s WHERE s.id = :setId ORDER BY r.setNumber")
	Page<Result> findBySet(Long setId, Pageable pageable);

	@Query("SELECT r FROM Result r INNER JOIN r.set s INNER JOIN s.train t WHERE t.id = :trainId ORDER BY r.createdOn DESC")
	Page<Result> findByTrain(Long trainId, Pageable pageable);

	@Query("SELECT r FROM Result r INNER JOIN r.set s INNER JOIN s.train t INNER JOIN t.sport sp WHERE sp.health.id = :healthId AND :date = r.date ORDER BY t.time DESC")
	Page<Result> findByDate(Instant date, Long healthId, Pageable pageable);

	@Query("SELECT r FROM Result r INNER JOIN r.set s INNER JOIN s.train t INNER JOIN t.sport sp WHERE sp.id = :sportId ORDER BY r.date DESC")
	Page<Result> findBySport(Long sportId, Pageable pageable);

	@Query("SELECT r FROM Result r INNER JOIN r.set s INNER JOIN s.train t INNER JOIN t.sport sp WHERE sp.health.id = :healthId ORDER BY r.date DESC")
	Page<Result> find(Long healthId, Pageable pageable);

//	day is useless @Query("SELECT r.date, t.day, sum(s.count*s.repeat) / 100 * sum(r.actualRepeat) FROM Result r RIGHT JOIN r.set s INNER JOIN s.train t INNER JOIN t.sport sp WHERE sp.id = :sportId GROUP BY CAST(r.date AS DATE) ORDER BY CAST(r.date AS DATE) DESC")
	@Query("SELECT new org.healthystyle.health.repository.result.DateStatistic(r.date, CAST(sum(s.count*s.repeat) / 100 * sum(r.actualRepeat) AS FLOAT)) FROM Result r RIGHT JOIN r.set s INNER JOIN s.train t INNER JOIN t.sport sp WHERE sp.id = :sportId GROUP BY CAST(r.date AS DATE) ORDER BY CAST(r.date AS DATE) DESC")
	Page<DateStatistic> findPercentageBySport(Long sportId, Pageable pageable);

//	day is useless @Query("SELECT r.date, t.day, sum(s.count*s.repeat) / 100 * sum(r.actualRepeat) FROM Result r RIGHT JOIN r.set s INNER JOIN s.train t WHERE t.id = :trainId GROUP BY CAST(r.date AS DATE) ORDER BY CAST(r.date AS DATE) DESC")
	@Query("SELECT new org.healthystyle.health.repository.result.DateStatistic(r.date, CAST(sum(s.count*s.repeat) / 100 * sum(r.actualRepeat) AS FLOAT)) FROM Result r RIGHT JOIN r.set s INNER JOIN s.train t WHERE t.id = :trainId GROUP BY CAST(r.date AS DATE) ORDER BY CAST(r.date AS DATE) DESC")
	Page<DateStatistic> findPercentageByTrain(Long trainId, Pageable pageable);

//	@Query(value = "SELECT t.time, TO_CHAR((SUM(r.actual_repeat) / (CAST(sum(s.count*s.repeat) AS FLOAT) / 100)), 'FM999999990.09') FROM train t INNER JOIN sport sp ON sp.id = t.sport_id INNER JOIN set s ON s.train_id = t.id LEFT JOIN set_result r ON r.set_id = s.id AND CAST(:date AS DATE) = r.executed_date WHERE sp.start <= CAST(:date AS DATE) AND sp.finish >= CAST(:date AS DATE) AND sp.health_id = 1 AND extract(isodow from CAST(:date AS DATE)) = t.day GROUP BY t.time ORDER BY t.time", nativeQuery = true)
	@Query(value = "SELECT t.time, TO_CHAR((SUM(r.sum_repeat) / (CAST(sum(s.count*s.repeat) AS FLOAT) / 100)), 'FM999999990.09') FROM train t INNER JOIN sport sp ON sp.id = t.sport_id INNER JOIN set s ON s.train_id = t.id LEFT JOIN LATERAL (SELECT set_id, sum(r.actual_repeat) AS sum_repeat FROM set_result r WHERE r.set_id = s.id AND r.executed_date = CAST(:date AS DATE) GROUP BY r.set_id) r ON r.set_id = s.id WHERE sp.start <= CAST(:date AS DATE) AND sp.finish >= CAST(:date AS DATE) AND sp.health_id = :healthId AND extract(isodow from CAST(:date AS DATE)) = t.day GROUP BY t.time ORDER BY t.time", nativeQuery = true)
	Page<Object[]> findPercentageByDate(LocalDate date, Long healthId, Pageable pageable);

//	@Query(value = "SELECT days, TO_CHAR((SUM(r.actual_repeat) / (CAST(sum(s.count*s.repeat) AS FLOAT) / 100)), 'FM999999990.09') FROM GENERATE_SERIES(DATE_TRUNC('week', CAST(:start AS TIMESTAMP)), DATE_TRUNC('week', CAST(:end AS TIMESTAMP)), interval '1' day) AS days LEFT JOIN sport sp ON days BETWEEN sp.start AND sp.finish INNER JOIN train t ON t.sport_id = sp.id AND t.day = EXTRACT(isodow FROM days) INNER JOIN set s ON s.train_id = t.id LEFT JOIN set_result r ON r.set_id = s.id AND r.executed_date = days WHERE sp.health_id = :healthId GROUP BY days ORDER BY days", nativeQuery = true)
	@Query(value = "SELECT days, TO_CHAR((SUM(r.sum_repeat) / (CAST(sum(s.count*s.repeat) AS FLOAT) / 100)), 'FM999999990.09') FROM GENERATE_SERIES(CAST(:start AS TIMESTAMP), CAST(:end AS TIMESTAMP), interval '1' day) AS days LEFT JOIN sport sp ON days BETWEEN sp.start AND sp.finish INNER JOIN train t ON t.sport_id = sp.id AND t.day = EXTRACT(isodow FROM days) INNER JOIN set s ON s.train_id = t.id LEFT JOIN LATERAL (SELECT r.set_id, SUM(r.actual_repeat) AS sum_repeat FROM set_result r WHERE r.set_id = s.id AND r.executed_date = days GROUP BY r.set_id) r ON r.set_id = s.id WHERE sp.health_id = :healthId GROUP BY days ORDER BY days", nativeQuery = true)
	Page<Object[]> findPercentageRangeDays(LocalDate start, LocalDate end, Long healthId, Pageable pageable);

//	@Query(value = "SELECT weeks, TO_CHAR((SUM(r.actual_repeat) / (CAST(sum(s.count*s.repeat) AS FLOAT) / 100)), 'FM999999990.09') FROM GENERATE_SERIES(DATE_TRUNC('week', CAST(:start AS TIMESTAMP)), DATE_TRUNC('week', CAST(:end AS TIMESTAMP)), interval '1 week') AS weeks LEFT JOIN sport sp ON (weeks, interval '1 week') OVERLAPS (sp.start, sp.finish) INNER JOIN train t ON t.sport_id = sp.id AND sp.start <= weeks + interval '1 day' * t.day AND sp.finish >= weeks + interval '1 day' * t.day INNER JOIN set s ON s.train_id = t.id LEFT JOIN set_result sr ON sr.set_id = s.id AND sr.executed_date BETWEEN weeks, weeks + interval '1 week' WHERE sp.health.id = :healthId GROUP BY weeks ORDER BY weeks", nativeQuery = true)
	@Query(value = "SELECT weeks, TO_CHAR((SUM(r.sum_repeat) / (CAST(sum(s.count*s.repeat) AS FLOAT) / 100)), 'FM999999990.09') FROM GENERATE_SERIES(DATE_TRUNC('week', CAST(:start AS TIMESTAMP)), DATE_TRUNC('week', CAST(:end AS TIMESTAMP)), interval '1 week') AS weeks LEFT JOIN sport sp ON (weeks, interval '1 week') OVERLAPS (sp.start, sp.finish) INNER JOIN train t ON t.sport_id = sp.id AND sp.start <= weeks + interval '1 day' * t.day AND sp.finish >= weeks + interval '1 day' * t.day INNER JOIN set s ON s.train_id = t.id LEFT JOIN LATERAL (SELECT r.set_id, SUM(r.actual_repeat) AS sum_repeat FROM set_result r WHERE r.set_id = s.id AND r.executed_date BETWEEN weeks AND weeks + interval '1 week' GROUP BY r.set_id) r ON r.set_id = s.id WHERE sp.health_id = :healthId GROUP BY weeks ORDER BY weeks", nativeQuery = true)
	Page<Object[]> findPercentageRangeWeeks(LocalDate start, LocalDate end, Long healthId, Pageable pageable);	

//	@Query(value = "SELECT weeks, TO_CHAR((SUM(r.actual_repeat) / (CAST(sum(s.count*s.repeat) AS FLOAT) / 100)), 'FM999999990.09') FROM GENERATE_SERIES(DATE_TRUNC('week', CAST(:start AS TIMESTAMP)), DATE_TRUNC('week', CAST(:end AS TIMESTAMP)), interval '1 week') AS weeks LEFT JOIN sport sp ON (weeks, interval '1 week') OVERLAPS (sp.start, sp.finish) INNER JOIN train t ON t.sport_id = sp.id AND sp.start <= weeks + interval '1 day' * t.day AND sp.finish >= weeks + interval '1 day' * t.day AND extract(isodow from weeks) = t.day INNER JOIN set s ON s.train_id = t.id LEFT JOIN set_result sr ON sr.set_id = s.id AND sr.executed_date = weeks WHERE sp.health.id = :healthId GROUP BY month ORDER BY month", nativeQuery = true)
	@Query(value = "SELECT date_trunc('month', date_trunc('week', weeks) + interval '1' day * (t.day - 1)), TO_CHAR((SUM(r.sum_repeat) / (CAST(sum(s.count*s.repeat) AS FLOAT) / 100)), 'FM999999990.09') FROM GENERATE_SERIES(DATE_TRUNC('week', CAST(:start AS TIMESTAMP)), DATE_TRUNC('week', CAST(:end AS TIMESTAMP)), interval '1 week') AS weeks LEFT JOIN sport sp ON (weeks, interval '1 week') OVERLAPS (sp.start, sp.finish) INNER JOIN train t ON t.sport_id = sp.id AND sp.start <= weeks + interval '1 day' * t.day AND sp.finish >= weeks + interval '1 day' * (t.day - 1) INNER JOIN set s ON s.train_id = t.id LEFT JOIN LATERAL (SELECT r.set_id, SUM(r.actual_repeat) AS sum_repeat FROM set_result r WHERE r.set_id = s.id AND r.executed_date BETWEEN weeks AND weeks + interval '1 week' GROUP BY r.set_id) r ON r.set_id = s.id WHERE sp.health_id = :healthId GROUP BY date_trunc('month', date_trunc('week', weeks) + interval '1' day * (t.day - 1)) ORDER BY date_trunc('month', date_trunc('week', weeks) + interval '1' day * (t.day - 1))", nativeQuery = true)
	Page<Object[]> findPercentageRangeMonths(LocalDate start, LocalDate end, Long healthId, Pageable pageable);

	@Query("SELECT EXISTS (SELECT r FROM Result r WHERE r.set.id = :setId AND r.date = :date AND r.setNumber = :setNumber)")
	boolean existsBySetAndDateAndNumber(Long setId, LocalDate date, Integer setNumber);

}
