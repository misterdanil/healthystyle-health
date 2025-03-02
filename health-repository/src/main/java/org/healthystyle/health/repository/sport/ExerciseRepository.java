package org.healthystyle.health.repository.sport;

import org.healthystyle.health.model.sport.Exerсise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository {
	@Query("SELECT e FROM Exercise e INNER JOIN e.health h WHERE e.title LIKE '%:title%' AND h.id = :healthId ORDER BY e.title")
	Page<Exerсise> findByTitle(String title, Long healthId, Pageable pageable);
	
	@Query("SELECT e FROM Exercise e INNER JOIN e.health h WHERE h.id = :healthId ORDER BY e.createdOn DESC")
	Page<Exerсise> find(Long healthId, Pageable pageable);
	
	
}
