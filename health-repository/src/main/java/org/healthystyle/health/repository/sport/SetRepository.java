package org.healthystyle.health.repository.sport;

import org.healthystyle.health.model.sport.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SetRepository {
	@Query("SELECT s FROM Set s INNER JOIN s.train t WHERE t.id = :trainId")
	Page<Set> findByTrain(Long trainId, Pageable pageable);

	@Query("SELECT s FROM Set s INNER JOIN s.exercise e WHERE e.id = :exerciseId")
	Page<Set> findByExercise(Long exerciseId, Pageable pageable);
}
