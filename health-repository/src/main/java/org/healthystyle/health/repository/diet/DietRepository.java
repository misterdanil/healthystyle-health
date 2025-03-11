package org.healthystyle.health.repository.diet;

import java.time.Instant;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.diet.Diet;
import org.healthystyle.health.model.sport.Sport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
	@Query("SELECT d FROM Diet d INNER JOIN d.health h WHERE d.title LIKE '%:title%' AND h.id = :healthId ORDER BY d.start <= CAST(CURRENT_TIMESTAMP AS DATE) AND d.end >= CAST(CURRENT_TIMESTAMP AS DATE), d.start")
	Page<Diet> findByTitle(String title, Long healthId, Pageable pageable);

	@Query("SELECT d FROM Diet d INNER JOIN d.health h WHERE d.start >= :start AND d.id = :healthId ORDER BY d.start")
	Page<Diet> findByStart(Instant start, Long healthId, Pageable pagealbe);

	@Query("SELECT d FROM Diet d INNER JOIN d.meals m INNER JOIN d.health h WHERE CAST(CURRENT_TIMESTAMP AS DATE) >= CAST(d.start AS DATE) AND CAST(CURRENT_TIMESTAMP AS DATE) <= CAST(d.end AS DATE) AND h.id = :healthId ORDER BY s.start")
	Page<Diet> findActual(Long healthId, Pageable pageable);

	@Query("SELECT d FROM Diet d INNER JOIN d.health h WHERE d.start >= :date AND d.end <= :date AND h.id = :healthId ORDER BY d.start")
	Page<Diet> findByDate(Instant date, Long healthId, Pageable pageable);

	@Query("SELECT EXISTS (SELECT d FROM Diet d INNER JOIN d.health h WHERE d.title = :title AND h.id = :healthId)")
	boolean existsByTitle(String title, Long healthId);
}
