package org.healthystyle.health.repository.medicine;

import org.healthystyle.health.model.medicine.Treatment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
	@Query("SELECT t FROM Treatment t INNER JOIN t.health h WHERE LOWER(t.description) LIKE LOWER('%:description%') AND h.id = :healthId ORDER BY t.createdOn DESC")
	Page<Treatment> findByDescription(String description, Long healthId, Pageable pageable);

	@Query("SELECT t FROM Treatment t INNER JOIN t.health h WHERE h.id = :healthId ORDER BY t.createdOn DESC")
	Page<Treatment> find(Long healthId, Pageable pageable);

	@Query("SELECT EXISTS (SELECT t FROM Treatment t INNER JOIN t.health h WHERE LOWER(t.description) = LOWER(:description) AND h.id = :healthId)")
	boolean existsByDescription(String description, Long healthId);
}
