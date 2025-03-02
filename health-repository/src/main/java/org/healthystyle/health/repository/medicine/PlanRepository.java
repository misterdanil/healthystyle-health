package org.healthystyle.health.repository.medicine;

import org.healthystyle.health.model.medicine.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
	@Query("SELECT p FROM Plan p INNER JOIN p.medicine m INNER JOIN m.health h WHERE m.id = :medicineId AND h.id = :healthId ORDER BY CAST(CURRENT_TIMESTAMP AS DATE) >= p.start AND CAST(CURRENT_TIMESTAMP AS DATE) <= p.end, p.start")
	Page<Plan> findByMedicine(Long medicineId, Long healthId, Pageable pageable);

	@Query("SELECT p FROM Plan p INNER JOIN p.health h WHERE h.id = :healthId ORDER BY p.start")
	Page<Plan> find(Long healthId, Pageable pageable);

	@Query("SELECT p FROM Plan p INNER JOIN p.treatment t INNER JOIN p.health h WHERE h.id = :healthId AND t.id = :treatmentId ORDER BY p.start")
	Page<Plan> findByTreatment(Long treatmentId, Long healthId);

}
