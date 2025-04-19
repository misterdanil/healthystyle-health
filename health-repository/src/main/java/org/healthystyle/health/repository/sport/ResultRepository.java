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

	@Query(value = "SELECT t.time, TO_CHAR(CAST(sum(s.count*s.repeat) AS FLOAT) / 100 * sum(r.actual_repeat), 'FM999999990.09') FROM set_result r RIGHT JOIN set s ON s.id = r.set_id INNER JOIN train t ON t.id = s.train_id INNER JOIN sport sp ON sp.id = t.sport_id WHERE sp.start <= :date AND sp.finish >= :date AND extract(isodow from CAST(:date AS DATE)) = t.day AND sp.health_id = :healthId GROUP BY t.time ORDER BY t.time", nativeQuery = true)
	Page<Object[]> findPercentageByDate(LocalDate date, Long healthId, Pageable pageable);

	@Query(value = "SELECT days, TO_CHAR(CAST(sum(s.count*s.repeat) AS FLOAT) / 100 * sum(r.actual_repeat), 'FM999999990.09') FROM GENERATE_SERIES(DATE_TRUNC('week', CAST(:start AS TIMESTAMP)), DATE_TRUNC('week', CAST(:end AS TIMESTAMP)), interval '1' day) AS days LEFT JOIN sport sp ON days BETWEEN sp.start AND sp.finish INNER JOIN train t ON t.sport_id = sp.id AND t.day = EXTRACT(isodow FROM days) INNER JOIN set s ON s.train_id = t.id LEFT JOIN set_result r ON r.set_id = s.id AND r.executed_date = days WHERE sp.health_id = :healthId GROUP BY days ORDER BY days", nativeQuery = true)
	Page<Object[]> findPercentageRangeDays(LocalDate start, LocalDate end, Long healthId, Pageable pageable);

	@Query(value = "SELECT new org.healthystyle.health.repository.result.DateStatistic(weeks, CAST(sum(s.count*s.repeat) / 100 * sum(r.actualRepeat) AS FLOAT)) FROM GENERATE_SERIES(DATE_TRUNC('week', :start::date), DATE_TRUNC('week', :end::date), interval '1' week) AS weeks LEFT JOIN set_result sr ON sr.date >= weeks AND sr.date < (weeks + interval '1' week) INNER JOIN set s ON s.id = sr.set_id INNER JOIN train t ON s.train_id = t.id INNER JOIN sport sp ON sp.id = t.sport_id WHERE sp.health.id = :healthId GROUP BY weeks ORDER BY weeks", nativeQuery = true)
	Page<DateStatistic> findPercentageRangeWeeks(LocalDate start, LocalDate end, Long healthId, Pageable pageable);

	@Query(value = "SELECT new org.healthystyle.health.repository.result.DateStatistic*months, sum(s.count*s.repeat) / 100 * sum(r.actualRepeat)) FROM GENERATE_SERIES(DATE_TRUNC('month', :start::date), DATE_TRUNC('month', :end::date), interval '1' month) AS months LEFT JOIN set_result sr ON sr.date >= months AND sr.date < (months + interval '1' month) INNER JOIN set s ON s.id = sr.set_id INNER JOIN train t ON s.train_id = t.id INNER JOIN sport sp ON sp.id = t.sport_id WHERE sp.health.id = :healthId GROUP BY months ORDER BY months", nativeQuery = true)
	Page<DateStatistic> findPercentageRangeMonths(LocalDate start, LocalDate end, Long healthId, Pageable pageable);

	@Query("SELECT EXISTS (SELECT r FROM Result r WHERE r.set.id = :setId AND r.date = :date AND r.setNumber = :setNumber)")
	boolean existsBySetAndDateAndNumber(Long setId, LocalDate date, Integer setNumber);

}
