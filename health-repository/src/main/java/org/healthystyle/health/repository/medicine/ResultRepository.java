package org.healthystyle.health.repository.medicine;

import java.time.LocalDate;

import org.healthystyle.health.model.medicine.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
	@Query("SELECT r FROM Result r INNER JOIN r.intake i WHERE i.id = :intakeId ORDER BY r.createdOn DESC")
	Page<Result> findByIntake(Long intakeId, Pageable pageable);

	@Query("SELECT r FROM Result r INNER JOIN r.intake i INNER JOIN i.plan p WHERE p.id = :planId ORDER BY r.createdOn DESC")
	Page<Result> findByPlan(Long planId, Pageable pageable);

	@Query("SELECT r FROM Result r INNER JOIN r.intake i INNER JOIN i.plan p INNER JOIN p.health h WHERE h.id = :healthId ORDER BY r.createdOn DESC")
	Page<Result> find(Long healthId, Pageable pageable);
	
	@Query("SELECT EXISTS (SELECT r FROM Result r INNER JOIN r.intake i WHERE i.id = :intakeId AND r.createdOn = :date)")
	boolean existsByIntakeAndDate(Long intakeId, LocalDate date);
}
