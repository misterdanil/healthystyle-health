package org.healthystyle.health.repository;

import org.healthystyle.health.model.Health;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRepository extends JpaRepository<Health, Long> {
	@Query("SELECT h FROM Health h WHERE h.userId = :userId")
	Health findByUserId(Long userId);
}
