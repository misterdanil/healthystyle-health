package org.healthystyle.health.repository.medicine;

import java.time.Instant;
import java.time.LocalDate;

import org.healthystyle.health.model.medicine.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
	@Query("SELECT p FROM Plan p INNER JOIN p.medicine m WHERE m.id = :medicineId AND p.start <= :end AND p.end >= :start")
	Plan findOverlaps(Long medicineId, Instant start, Instant end);

	@Query(value = "SELECT p FROM plan p INNER JOIN medicine m ON m.id = p.medicine_id WHERE m.id = :medicineId AND m.health_id = :healthId ORDER BY CAST(CURRENT_TIMESTAMP AS DATE) >= p.start AND CAST(CURRENT_TIMESTAMP AS DATE) <= p.end DESC, p.start", nativeQuery = true)
	Page<Plan> findByMedicine(Long medicineId, Long healthId, Pageable pageable);

	@Query("SELECT p FROM Plan p INNER JOIN p.health h WHERE h.id = :healthId AND CURRENT_DATE >= p.start AND CURRENT_DATE <= p.end ORDER BY p.start")
	Page<Plan> findActual(Long healthId, Pageable pageable);

	@Query("SELECT p FROM Plan p INNER JOIN p.health h WHERE h.id = :healthId ORDER BY p.start DESC")
	Page<Plan> find(Long healthId, Pageable pageable);

	@Query("SELECT p FROM Plan p INNER JOIN p.treatment t INNER JOIN p.health h WHERE h.id = :healthId AND t.id = :treatmentId ORDER BY p.start")
	Page<Plan> findByTreatment(Long treatmentId, Long healthId, Pageable pageable);

	@Query("SELECT EXISTS (SELECT p FROM Plan p INNER JOIN p.medicine m WHERE m.id = :medicineId AND p.start <= :end AND p.end >= :start AND (:planId IS NULL OR p.id != :planId))")
	boolean existsOverlaps(LocalDate start, LocalDate end, Long medicineId, Long planId);

}
