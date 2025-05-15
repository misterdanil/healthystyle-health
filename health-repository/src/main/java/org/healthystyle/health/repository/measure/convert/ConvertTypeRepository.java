package org.healthystyle.health.repository.measure.convert;

import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.model.measure.convert.FloatNumber;
import org.healthystyle.health.model.measure.convert.IntegerNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvertTypeRepository extends JpaRepository<ConvertType, Long> {
	FloatNumber findByRange(int range);
	
	@Query("SELECT ct FROM ConvertType ct WHERE TYPE(ct) = IntegerNumber")
	IntegerNumber findInteger();
}
