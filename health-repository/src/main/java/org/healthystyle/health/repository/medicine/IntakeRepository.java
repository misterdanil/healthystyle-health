package org.healthystyle.health.repository.medicine;

import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.healthystyle.health.model.medicine.Intake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IntakeRepository extends JpaRepository<Intake, Long> {
	@Query("SELECT Intake i INNER JOIN i.plan p INNER JOIN p.medicine m INNER JOIN m.health h WHERE m.name LIKE '%:name%' AND h.id = :healthId ORDER BY p.start <= CAST(CURRENT_TIMESTAMP AS DATE) AND p.end >= CAST(CURRENT_TIMESTAMP AS DATE) DESC, p.start, i.day >= extract(dow FROM CURRENT_TIMESTAMP) DESC, i.day, i.time >= CURRENT_TIME DESC, i.time")
	Page<Intake> findByMedicine(String name, Long healthId, Pageable pageable);

	@Query("SELECT Intake i INNER JOIN i.plan p WHERE p.id = :planId ORDER BY i.day, i.time")
	Page<Intake> findByPlan(Long planId, Pageable pageable);

	@Query("SELECT i FROM Intake i INNER JOIN i.plan p INNER JOIN p.health h WHERE i.day = :day AND h.id = :healthId AND p.start <= CAST(CURRENT_TIMESTAMP AS DATE) AND p.end >= CAST(CURRENT_TIMESTAMP AS DATE) ORDER BY t.time")
	Page<Intake> findByDay(Integer day, Long healthId, Pageable pageable);

	@Query("SELECT i FROM Intake i INNER JOIN i.plan p INNER JOIN p.health h WHERE CAST(:date AS date) BETWEEN p.start AND p.end AND i.day = extract(dow FROM :date) AND h.id = :healthId ORDER BY i.day, i.time")
	Page<Intake> findByDate(Instant date, Long healthId, Pageable pageable);

	@Query("SELECT i FROM Intake i INNER JOIN i.plan p INNER JOIN p.health h WHERE CURRENT_DATE BETWEEN p.start AND p.end AND i.day = extract(dow FROM CURRENT_DATE) AND h.id = :healthId ORDER BY i.time")
	Page<Intake> findByCurrentDate(Long healthId, Pageable pageable);

	@Query("SELECT i FROM Intake i INNER JOIN i.plan p INNER JOIN p.health h WHERE h.id = :healthId AND p.end >= CAST(CURRENT_TIMESTAMP AS DATE) ORDER BY p.start, t.day >= (dow FROM CURRENT_TIMESTAMP) DESC, t.day, t.time >= CURRENT_TIME, t.time")
	Page<Intake> findPlanned(Long healthId, Pageable pageable);

	@Query(value = "WITH nextIntakeTime AS ("
			+ "SELECT i.time FROM intake i INNER JOIN plan p ON p.plan_id = p.id INNER JOIN health h ON h.id = p.health_id WHERE CURRENT_DATE BETWEEN d.start AND d.end AND i.day = extract(dow FROM CURRENT_DATE) AND i.time >= CURRENT_TIME AND h.id = :healthId ORDER BY i.time DESC LIMIT 1)"
			+ ")"
			+ "SELECT i FROM intake i INNER JOIN plan p ON i.plan_id = p.id WHERE CURRENT_DATE BETWEEN d.start AND d.end AND i.day = extract(dow FROM CURRENT_DATE) AND i.time == nextMealTime OFFSET (:page - 1) LIMIT :limit")
	Page<Intake> findNextIntake(Long healthId, int page, int limit);
	
	@Query(value = "SELECT d + m.time FROM plan p INNER JOIN health h ON h.id = p.health_id INNER JOIN intake i on i.plan_id = p.id INNER JOIN GENERATE_SERIES(p.start, p.end, interval '1' day) AS d ON extract(isodow from d) = m.day LEFT JOIN intake_result r ON r.intake_id = i.id AND EXTRACT(day FROM r.created_on) = EXTRACT(day FROM d) WHERE h.id = :healthId AND r IS NULL ORDER BY d DESC OFFSET (:page - 1) LIMIT :limit", nativeQuery = true)
	Page<Intake> findNotExecuted(Long healthId, int page, int limit);

	@Query("SELECT EXISTS (SELECT i FROM Intake i INNER JOIN i.plan p WHERE i.time = :time AND i.day = :day AND p.id = :planId)")
	boolean existsByTimeAndDayAndPlanId(LocalTime time, Integer day, Long planId);

}
