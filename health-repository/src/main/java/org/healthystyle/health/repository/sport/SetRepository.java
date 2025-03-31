package org.healthystyle.health.repository.sport;

import org.healthystyle.health.model.sport.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SetRepository extends JpaRepository<Set, Long> {
	@Query("SELECT s FROM Set s INNER JOIN s.train t WHERE t.id = :trainId")
	Page<Set> findByTrain(Long trainId, Pageable pageable);

	@Query("SELECT s FROM Set s INNER JOIN s.exercise e WHERE e.id = :exerciseId AND e.health.id = :healthId")
	Page<Set> findByExercise(Long exerciseId, Long healthId, Pageable pageable);
}
