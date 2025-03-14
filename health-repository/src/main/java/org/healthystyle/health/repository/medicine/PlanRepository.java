package org.healthystyle.health.repository.medicine;

import java.time.Instant;

import org.healthystyle.health.model.medicine.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
	@Query("SELECT p FROM Plan p INNER JOIN p.medicine m WHERE m.id = :medicineId AND (p.start, p.end) OVERLAPS (:start, :end)")
	Plan findOverlaps(Long medicineId, Instant start, Instant end);

	@Query("SELECT p FROM Plan p INNER JOIN p.medicine m INNER JOIN m.health h WHERE m.id = :medicineId AND h.id = :healthId ORDER BY CAST(CURRENT_TIMESTAMP AS DATE) >= p.start AND CAST(CURRENT_TIMESTAMP AS DATE) <= p.end, p.start")
	Page<Plan> findByMedicine(Long medicineId, Long healthId, Pageable pageable);

	@Query("SELECT p FROM Plan p INNER JOIN p.health h WHERE h.id = :healthId AND CURRENT_DATE >= p.start AND CURRENT_DATE <= p.end ORDER BY p.start")
	Page<Plan> findActual(Long healthId, Pageable pageable);

	@Query("SELECT p FROM Plan p INNER JOIN p.health h WHERE h.id = :healthId ORDER BY p.start DESC")
	Page<Plan> find(Long healthId, Pageable pageable);

	@Query("SELECT p FROM Plan p INNER JOIN p.treatment t INNER JOIN p.health h WHERE h.id = :healthId AND t.id = :treatmentId ORDER BY p.start")
	Page<Plan> findByTreatment(Long treatmentId, Long healthId, Pageable pageable);

	@Query("SELECT EXISTS (SELECT p FROM Plan p INNER JOIN p.medicine m WHERE m.id = :medicineId AND (p.start, p.end) overlaps (:start, :end) AND (:planId IS NULL OR p.id != :planId))")
	boolean existsOverlaps(Instant start, Instant end, Long medicineId, Long planId);

}
