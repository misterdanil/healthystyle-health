package org.healthystyle.health.repository.sport;

import org.healthystyle.health.model.sport.Sport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
	@Query(value = "SELECT s FROM sport s WHERE LOWER(s.description) LIKE LOWER('%:description%') AND s.health_id = :healthId ORDER BY CAST(CURRENT_TIMESTAMP AS DATE) >= s.start AND CAST(CURRENT_TIMESTAMP AS DATE) <= s.end, s.start", nativeQuery = true)
	Page<Sport> findByDescription(String description, Long healthId, Pageable pageable);

	@Query("SELECT s FROM Sport s INNER JOIN s.trains t WHERE CAST(CURRENT_TIMESTAMP AS DATE) >= CAST(s.start AS DATE) AND CAST(CURRENT_TIMESTAMP AS DATE) <= CAST(s.end AS DATE) AND s.health.id = :healthId ORDER BY s.start")
	Page<Sport> findActual(Long healthId, Pageable pageable);
}
