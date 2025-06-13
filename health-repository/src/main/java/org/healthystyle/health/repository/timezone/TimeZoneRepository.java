package org.healthystyle.health.repository.timezone;

import org.healthystyle.health.model.Timezone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeZoneRepository extends JpaRepository<Timezone, Long> {
	Timezone findByUserId(Long userId);
	
	boolean existsByUserId(Long userId);
}
