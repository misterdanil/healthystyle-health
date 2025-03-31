package org.healthystyle.health.repository.sport;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

import org.healthystyle.health.model.sport.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
	@Query("SELECT t FROM Train t INNER JOIN t.sport s INNER JOIN s.health h WHERE t.description LIKE '%:description%' AND h.id = :healthId ORDER BY s.start <= CAST(CURRENT_TIMESTAMP AS DATE) AND s.end >= CAST(CURRENT_TIMESTAMP AS DATE) DESC, s.start, t.day >= (dow FROM CURRENT_TIMESTAMP) DESC, t.day, t.time >= CURRENT_TIME DESC, t.time")
	Page<Train> findByDescription(String description, Long healthId, Pageable pageable);

	@Query("SELECT t FROM Train t INNER JOIN t.sport s INNER JOIN s.health h WHERE t.day = :day AND h.id = :healthId AND s.start <= CAST(CURRENT_TIMESTAMP AS DATE) AND s.end >= CAST(CURRENT_TIMESTAMP AS DATE) ORDER BY t.time")
	Page<Train> findByDay(Integer day, Long healthId, Pageable pageable);

	@Query("SELECT t FROM Train t INNER JOIN t.sport s WHERE s.start <= :date AND s.end >= :date AND extract(dow from :date) = t.day ORDER BY t.time")
	Page<Train> findByDate(Instant date, Long healthId, Pageable pageable);

	@Query("SELECT t FROM Train t INNER JOIN t.sport s INNER JOIN s.health h WHERE s.id = :sportId AND h.id = :healthId ORDER BY t.day, t.time")
	Page<Train> findBySport(Long sportId, Pageable pageable);

	@Query("SELECT t FROM Train t INNER JOIN t.sport s INNER JOIN s.health h WHERE h.id = :healthId AND s.end >= CAST(CURRENT_TIMESTAMP AS DATE) ORDER BY s.start, t.day >= (dow FROM CURRENT_TIMESTAMP) DESC, t.day, t.time >= CURRENT_TIME DESC, t.time")
	Page<Train> findPlanned(Long healthId, Pageable pageable);

	@Query(value = "WITH nextTrainTime AS ("
			+ "SELECT t.time FROM train t INNER JOIN sport s ON t.sport_id = s.id WHERE CURRENT_DATE BETWEEN s.start AND s.end AND t.day = extract(dow FROM CURRENT_DATE) AND t.time >= CURRENT_TIME AND s.health_id = :healthId ORDER BY m.time LIMIT 1)"
			+ ")"
			+ "SELECT t FROM train t INNER JOIN sport s ON t.sport_id = s.id WHERE CURRENT_DATE BETWEEN s.start AND s.end AND t.day = extract(dow FROM CURRENT_DATE) AND t.time == nextTrainTime AND s.health_id = :healthId")
	List<Train> findNextTrain(Long healthId);
	
	@Query("SELECT EXISTS (SELECT t FROM Train t WHERE t.day = :day AND t.time = :time)")
	boolean existsByDayAndTime(Integer day, LocalTime time);
}
