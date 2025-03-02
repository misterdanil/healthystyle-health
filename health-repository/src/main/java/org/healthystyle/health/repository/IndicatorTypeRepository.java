package org.healthystyle.health.repository;

import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicatorTypeRepository extends JpaRepository<IndicatorType, Long> {
	@Query("SELECT it FROM IndicatorType it WHERE it.name LIKE '%name%'")
	Page<IndicatorType> findByName(String name, Pageable pageable);

	@Query("SELECT it FROM IndicatorType it INNER JOIN it.measure m WHERE m.type = :type")
	Page<IndicatorType> findByMeasure(Type type, Pageable pageable);

	@Query("SELECT it FROM IndicatorType WHERE it.convertType = :convertType")
	Page<IndicatorType> findByConvertType(ConvertType convertType);
}
