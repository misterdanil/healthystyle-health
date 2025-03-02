package org.healthystyle.health.repository.medicine;

import org.healthystyle.health.model.medicine.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
	@Query("SELECT r FROM Result r INNER JOIN r.intake i WHERE i.id = :intakeId ORDER BY r.createdOn DESC")
	Page<Result> findByIntake(Long intakeId);

	@Query("SELECT r FROM Result r INNER JOIN r.intake i INNER JOIN i.plan p INNER JOIN p.health h WHERE p.id = :planId AND h.id = :healthId ORDER BY r.createdOn DESC")
	Page<Result> findByPlan(Long planId, Long healthId, Pageable pageable);

	@Query("SELECT r FROM Result r INNER JOIN r.intake i INNER JOIN i.plan p INNER JOIN p.health h WHERE h.id = :healthId ORDER BY r.createdOn DESC")
	Page<Result> findLastCreated(Long healthId, Pageable pageable);
}
