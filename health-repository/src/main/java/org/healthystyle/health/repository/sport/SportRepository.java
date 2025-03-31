package org.healthystyle.health.repository.sport;

import org.healthystyle.health.model.sport.Sport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
	@Query("SELECT s FROM Sport s INNER JOIN s.health h WHERE s.description LIKE '%:description%' AND h.id = :healthId ORDER BY CAST(CURRENT_TIMESTAMP AS DATE) >= s.start AND CAST(CURRENT_TIMESTAMP AS DATE) <= s.end, s.start")
	Page<Sport> findByDescription(String description, Long healthId, Pageable pageable);

	@Query("SELECT s FROM Sport s INNER JOIN s.trains t INNER JOIN s.health h WHERE CAST(CURRENT_TIMESTAMP AS DATE) >= CAST(s.start AS DATE) AND CAST(CURRENT_TIMESTAMP AS DATE) <= CAST(s.end AS DATE) AND h.id = :healthId ORDER BY s.start")
	Page<Sport> findActual(Long healthId, Pageable pageable);
}
