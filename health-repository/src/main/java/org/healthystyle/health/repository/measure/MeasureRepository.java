package org.healthystyle.health.repository.measure;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Long> {
	@Query("SELECT m FROM Measure m WHERE m.type = :type")
	Measure findByType(Type type);
}
