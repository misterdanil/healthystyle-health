package org.healthystyle.health.repository.measure.convert;

import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.model.measure.convert.FloatNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvertTypeRepository extends JpaRepository<ConvertType, Long> {
	FloatNumber findByRanges(int ranges);
}
