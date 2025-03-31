package org.healthystyle.health.repository.sport;

import org.healthystyle.health.model.sport.Step;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {
	@Query("SELECT s FROM Step s INNER JOIN s.exercise e WHERE e.id = :exerciseId")
	Page<Step> findByExercise(Long exerciseId, Pageable pageable);
}
