package org.healthystyle.health.repository.medicine;

import java.sql.Date;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.medicine.Intake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IntakeRepository extends JpaRepository<Intake, Long> {
	@Query(value = "SELECT intake i INNER JOIN plan p ON p.id = i.plan_id INNER JOIN medicine m On p.medicine_id = m.id WHERE LOWER(m.name) LIKE LOWER('%:name%') AND p.health_id = :healthId ORDER BY p.start <= CAST(CURRENT_TIMESTAMP AS DATE) AND p.end >= CAST(CURRENT_TIMESTAMP AS DATE) DESC, p.start, i.day >= extract(isodow FROM CURRENT_TIMESTAMP) DESC, i.day, i.time >= CURRENT_TIME DESC, i.time OFFSET (:page - 1) * :limit LIMIT :limit", nativeQuery = true)
	Page<Intake> findByMedicine(String name, Long healthId, Pageable pageable);

	@Query("SELECT i FROM Intake i WHERE i.plan.id = :planId ORDER BY i.day, i.time")
	Page<Intake> findByPlan(Long planId, Pageable pageable);

	@Query("SELECT i FROM Intake i INNER JOIN i.plan p INNER JOIN p.health h WHERE i.day = :day AND h.id = :healthId AND p.start <= CAST(CURRENT_TIMESTAMP AS DATE) AND p.end >= CAST(CURRENT_TIMESTAMP AS DATE) ORDER BY i.time ")
	Page<Intake> findByDay(Integer day, Long healthId, Pageable pageable);

	@Query(value = "SELECT i FROM intake i INNER JOIN plan p ON p.id = i.plan_id WHERE CAST(:date AS date) BETWEEN p.start AND p.end AND i.day = extract(isodow FROM :date) AND p.health_id = :healthId ORDER BY i.day, i.time OFFSET (:page - 1) * :limit LIMIT :limit", nativeQuery = true)
	List<Intake> findByDate(Instant date, Long healthId, int page, int limit);

	@Query(value = "SELECT i FROM intake i INNER JOIN plan p ON p.id = i.plan_id WHERE CURRENT_DATE BETWEEN p.start AND p.end AND i.day = extract(isodow FROM CURRENT_DATE) AND p.health_id = :healthId ORDER BY i.time OFFSET (:page - 1) * :limit LIMIT :limit", nativeQuery = true)
	List<Intake> findByCurrentDate(Long healthId, int page, int limit);

	@Query(value = "SELECT i.* FROM intake i INNER JOIN plan p ON p.id = i.plan_id WHERE p.health_id = :healthId AND p.finish >= CAST(CURRENT_TIMESTAMP AS DATE) ORDER BY i.day >= extract(isodow FROM CURRENT_TIMESTAMP) DESC, i.day, i.time >= CURRENT_TIME, i.time OFFSET (:page - 1) * :limit LIMIT :limit", nativeQuery = true)
	List<Intake> findPlanned(Long healthId, int page, int limit);

	@Query(value = "WITH nextIntakeTime AS ("
			+ "SELECT i.time FROM intake i INNER JOIN plan p ON i.plan_id = p.id INNER JOIN health h ON h.id = p.health_id WHERE CURRENT_DATE BETWEEN p.start AND p.finish AND i.day = extract(isodow FROM CURRENT_DATE) AND i.time >= CURRENT_TIME AND h.id = :healthId ORDER BY i.time DESC LIMIT 1"
			+ ")"
			+ "SELECT i.* FROM intake i INNER JOIN plan p ON i.plan_id = p.id INNER JOIN nextIntakeTime nit ON nit.time = i.time LEFT JOIN intake_result ir ON ir.intake_id = i.id WHERE ir.id IS NULL AND CURRENT_DATE BETWEEN p.start AND p.finish AND i.day = extract(isodow FROM CURRENT_DATE) OFFSET (:page - 1) * :limit LIMIT :limit", nativeQuery = true)
	List<Intake> findNextIntake(Long healthId, int page, int limit);

	@Query(value = "SELECT i.id, p.start, p.finish, i.time, i.day, i.weight, meas.type, m.name, t.description, d + i.time FROM plan p INNER JOIN medicine m ON p.medicine_id = m.id INNER JOIN health h ON h.id = p.health_id INNER JOIN intake i on i.plan_id = p.id INNER JOIN measure meas ON meas.id = i.measure_id INNER JOIN GENERATE_SERIES(p.start, CURRENT_DATE, interval '1' day) AS d ON extract(isodow from d) = i.day LEFT JOIN intake_result r ON r.intake_id = i.id AND EXTRACT(day FROM r.created_on) = EXTRACT(day FROM d) INNER JOIN treatment t ON p.treatment_id = t.id WHERE h.id = :healthId AND r IS NULL ORDER BY d DESC OFFSET (:page - 1) * :limit LIMIT :limit", nativeQuery = true)
	List<MissedDateIntake> findNotExecuted(Long healthId, int page, int limit);

	@Query("SELECT EXISTS (SELECT i FROM Intake i INNER JOIN i.plan p WHERE i.time = :time AND i.day = :day AND p.id = :planId)")
	boolean existsByTimeAndDayAndPlanId(LocalTime time, Integer day, Long planId);

	public static class MissedDateIntake {
		private Long id;
		private LocalDate planStart;
		private LocalDate planEnd;
		private LocalTime time;
		private Integer day;
		private String weight;
		private Type measure;
		private String medicineName;
		private String treatmentDescription;
		private Instant date;

		public MissedDateIntake() {
			super();
		}

		public MissedDateIntake(Long id, Date planStart, Date planEnd, Time time, Integer day, String weight,
				String measure, String medicineName, String treatmentDescription, Instant date) {
			super();
			this.id = id;
			this.planStart = Instant.ofEpochMilli(planStart.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
			this.planEnd = Instant.ofEpochMilli(planEnd.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
			this.time = Instant.ofEpochMilli(time.getTime()).atZone(ZoneId.systemDefault()).toLocalTime();
			this.day = day;
			this.weight = weight;
			this.measure = Type.valueOf(measure);
			this.medicineName = medicineName;
			this.treatmentDescription = treatmentDescription;
			this.date = date;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public LocalDate getPlanStart() {
			return planStart;
		}

		public void setPlanStart(LocalDate planStart) {
			this.planStart = planStart;
		}

		public LocalDate getPlanEnd() {
			return planEnd;
		}

		public void setPlanEnd(LocalDate planEnd) {
			this.planEnd = planEnd;
		}

		public LocalTime getTime() {
			return time;
		}

		public void setTime(LocalTime time) {
			this.time = time;
		}

		public Integer getDay() {
			return day;
		}

		public void setDay(Integer day) {
			this.day = day;
		}

		public String getWeight() {
			return weight;
		}

		public void setWeight(String weight) {
			this.weight = weight;
		}

		public Type getMeasure() {
			return measure;
		}

		public void setMeasure(Type measure) {
			this.measure = measure;
		}

		public String getMedicineName() {
			return medicineName;
		}

		public void setMedicineName(String medicineName) {
			this.medicineName = medicineName;
		}

		public String getTreatmentDescription() {
			return treatmentDescription;
		}

		public void setTreatmentDescription(String treatmentDescription) {
			this.treatmentDescription = treatmentDescription;
		}

		public Instant getDate() {
			return date;
		}

		public void setDate(Instant date) {
			this.date = date;
		}

	}
}
