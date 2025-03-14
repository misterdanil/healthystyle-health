package org.healthystyle.health.repository.medicine;

import org.healthystyle.health.model.medicine.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
	@Query("SELECT m FROM Medicine m INNER JOIN m.health h WHERE m.name LIKE '%:name%' AND h.id = :healthId")
	Page<Medicine> findByName(String name, Long healthId, Pageable pageable);

	@Query("SELECT m FROM Medicine m INNER JOIN m.health h WHERE h.id = :healthId ORDER BY m.createdOn DESC")
	Page<Medicine> find(Long healthId, Pageable pageable);
	
	boolean existsByName(String name, Long healthId);
}
