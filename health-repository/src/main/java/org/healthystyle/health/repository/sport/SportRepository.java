package org.healthystyle.health.repository.sport;

import java.util.Set;

import org.healthystyle.health.model.sport.Sport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
	@Query(value = "SELECT s.*, t.id AS train_id, t.description AS train_description, t.day, t.time, st.id AS set_id, st.count, st.repeat, e.id AS exercise_id, e.title FROM sport s INNER JOIN train t ON s.id = t.sport_id INNER JOIN set st ON st.train_id = t.id INNER JOIN exercise e ON e.id = st.exercise_id WHERE LOWER(s.description) LIKE CONCAT('%', LOWER(:description), '%') AND s.health_id = :healthId ORDER BY CURRENT_DATE >= s.start AND CURRENT_DATE <= s.finish, s.start OFFSET (:page - 1) * :limit LIMIT :limit", nativeQuery = true)
	Set<Sport> findByDescription(String description, Long healthId, int page, int limit);

	@Query("SELECT s FROM Sport s INNER JOIN s.trains t WHERE CAST(CURRENT_TIMESTAMP AS DATE) >= CAST(s.start AS DATE) AND CAST(CURRENT_TIMESTAMP AS DATE) <= CAST(s.end AS DATE) AND s.health.id = :healthId ORDER BY s.start")
	Page<Sport> findActual(Long healthId, Pageable pageable);
}
