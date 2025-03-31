package org.healthystyle.health.repository.sport;

import org.healthystyle.health.model.sport.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
	@Query("SELECT e FROM Exercise e INNER JOIN e.health h WHERE e.title LIKE '%:title%' AND h.id = :healthId ORDER BY e.title")
	Page<Exercise> findByTitle(String title, Long healthId, Pageable pageable);

	@Query("SELECT e FROM Exercise e INNER JOIN e.health h WHERE h.id = :healthId ORDER BY e.createdOn DESC")
	Page<Exercise> find(Long healthId, Pageable pageable);

	@Query("SELECT EXISTS (SELECT e FROM Exercise e INNER JOIN e.health h WHERE LOWER(e.title) = (:title) AND h.id = :healthId)")
	boolean existsByTitle(String title, Long healthId);

}
