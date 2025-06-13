package org.healthystyle.health.repository.sport;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.healthystyle.health.model.sport.Train;
import org.healthystyle.health.repository.medicine.IntakeRepository.MissedDateIntake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
	@Query(value = "SELECT t FROM train t INNER JOIN sport s ON s.id = t.sport_id WHERE LOWER(t.description) LIKE LOWER('%:description%') AND s.health_id = :healthId ORDER BY s.start <= CAST(CURRENT_TIMESTAMP AS DATE) AND s.end >= CAST(CURRENT_TIMESTAMP AS DATE) DESC, s.start, t.day >= (isodow FROM CURRENT_TIMESTAMP) DESC, t.day, t.time >= CURRENT_TIME DESC, t.time", nativeQuery = true)
	Page<Train> findByDescription(String description, Long healthId, Pageable pageable);

	@Query("SELECT t FROM Train t INNER JOIN t.sport s WHERE t.day = :day AND s.health.id = :healthId AND s.start <= CAST(CURRENT_TIMESTAMP AS DATE) AND s.end >= CAST(CURRENT_TIMESTAMP AS DATE) ORDER BY t.time")
	Page<Train> findByDay(Integer day, Long healthId, Pageable pageable);

	@Query(value = "SELECT t FROM train t INNER JOIN sport s ON s.id = t.sport_id WHERE s.start <= :date AND s.end >= :date AND extract(isodow FROM :date) = t.day ORDER BY t.time", nativeQuery = true)
	Page<Train> findByDate(Instant date, Long healthId, Pageable pageable);

	@Query("SELECT t FROM Train t INNER JOIN t.sport s INNER JOIN s.health h WHERE s.id = :sportId AND h.id = :healthId ORDER BY t.day, t.time")
	Page<Train> findBySport(Long sportId, Pageable pageable);

	@Query(value = "SELECT t.*, s.description AS sport_description, st.id AS set_id, st.count, st.repeat, e.title FROM train t INNER JOIN sport s ON s.id = t.sport_id INNER JOIN set st ON st.train_id = t.id INNER JOIN exercise e ON e.id = st.exercise_id WHERE s.health_id = :healthId AND s.finish >= CAST(CURRENT_TIMESTAMP AS DATE) ORDER BY t.day >= extract(isodow FROM CURRENT_TIMESTAMP) DESC, t.day, t.time >= CURRENT_TIME DESC, t.time OFFSET (:page - 1) * :limit LIMIT :limit", nativeQuery = true)
	Set<Train> findPlanned(Long healthId, int page, int limit);

	@Query(value = "WITH nextTrainTime AS ("
			+ "SELECT t.time FROM train t INNER JOIN sport s ON t.sport_id = s.id WHERE CURRENT_DATE BETWEEN s.start AND s.end AND t.day = extract(isodow FROM CURRENT_DATE) AND t.time >= CURRENT_TIME AND s.health_id = :healthId ORDER BY m.time LIMIT 1)"
			+ ")"
			+ "SELECT t FROM train t INNER JOIN sport s ON t.sport_id = s.id WHERE CURRENT_DATE BETWEEN s.start AND s.end AND t.day = extract(isodow FROM CURRENT_DATE) AND t.time == nextTrainTime AND s.health_id = :healthId", nativeQuery = true)
	List<Train> findNextTrain(Long healthId);

	@Query(value = "SELECT st.id, s.start, s.finish, t.time, t.day, st.count, st.repeat, e.title, s.description, CAST(d AS DATE) + t.time, h.user_id FROM sport s INNER JOIN train t ON t.sport_id = s.id INNER JOIN set st ON t.id = st.train_id INNER JOIN exercise e ON st.exercise_id = e.id INNER JOIN health h ON h.id = s.health_id INNER JOIN timezone tz ON tz.user_id = h.user_id INNER JOIN GENERATE_SERIES(:start AT TIME ZONE tz.timezone, :end AT TIME ZONE tz.timezone, interval '1' hour) AS d ON extract(isodow from d) = t.day AND extract(hour from d) = extract(hour from t.time) WHERE s.start <= CURRENT_DATE AT TIME ZONE tz.timezone AND s.finish >= CURRENT_DATE AT TIME ZONE tz.timezone ORDER BY CAST(d AS DATE) + t.time DESC", nativeQuery = true)
	List<MissedTrain> findNotExecuted(Instant start, Instant end);

	@Query("SELECT EXISTS (SELECT t FROM Train t WHERE t.day = :day AND t.time = :time AND t.sport.id = :sportId)")
	boolean existsByDayAndTime(Integer day, LocalTime time, Long sportId);

	public static class MissedTrain {
		private Long id;
		private LocalDate sportStart;
		private LocalDate sportEnd;
		private LocalTime time;
		private Integer day;
		private Integer count;
		private Integer repeat;
		private String exercise;
		private String sportDescription;
		private LocalDateTime date;
		private Long userId;

		public MissedTrain(Long id, LocalDate sportStart, LocalDate sportEnd, LocalTime time, Integer day,
				Integer count, Integer repeat, String exercise, String sportDescription, Timestamp date,
				Long userId) {
			super();
			this.id = id;
			this.sportStart = sportStart;
			this.sportEnd = sportEnd;
			this.time = time;
			this.day = day;
			this.count = count;
			this.repeat = repeat;
			this.exercise = exercise;
			this.sportDescription = sportDescription;
			this.date = date.toLocalDateTime();
			this.userId = userId;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public LocalDate getSportStart() {
			return sportStart;
		}

		public void setSportStart(LocalDate sportStart) {
			this.sportStart = sportStart;
		}

		public LocalDate getSportEnd() {
			return sportEnd;
		}

		public void setSportEnd(LocalDate sportEnd) {
			this.sportEnd = sportEnd;
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

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public Integer getRepeat() {
			return repeat;
		}

		public void setRepeat(Integer repeat) {
			this.repeat = repeat;
		}

		public String getExercise() {
			return exercise;
		}

		public void setExercise(String exercise) {
			this.exercise = exercise;
		}

		public String getSportDescription() {
			return sportDescription;
		}

		public void setSportDescription(String sportDescription) {
			this.sportDescription = sportDescription;
		}

		public LocalDateTime getDate() {
			return date;
		}

		public void setDate(LocalDateTime date) {
			this.date = date;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

	}
}
