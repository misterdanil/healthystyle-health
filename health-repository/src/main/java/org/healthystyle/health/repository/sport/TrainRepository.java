package org.healthystyle.health.repository.sport;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.healthystyle.health.model.sport.Train;
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

	@Query("SELECT EXISTS (SELECT t FROM Train t WHERE t.day = :day AND t.time = :time AND t.sport.id = :sportId)")
	boolean existsByDayAndTime(Integer day, LocalTime time, Long sportId);
}
