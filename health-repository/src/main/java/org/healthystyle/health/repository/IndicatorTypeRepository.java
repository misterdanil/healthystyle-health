package org.healthystyle.health.repository;

import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicatorTypeRepository extends JpaRepository<IndicatorType, Long> {
	@Query("SELECT it FROM IndicatorType it WHERE LOWER(it.name) LIKE CONCAT(LOWER(:name), '%') ORDER BY it.name, it.createdOn DESC")
	Page<IndicatorType> findByName(String name, Pageable pageable);

	@Query("SELECT it FROM IndicatorType it")
	Page<IndicatorType> find(Pageable pageable);

	@Query(value = "SELECT it.*, MAX(i.created_on) FROM indicator_type it INNER JOIN indicator i ON i.indicator_type_id = it.id GROUP BY it.id ORDER by MAX(i.created_on) DESC", nativeQuery = true)
	Page<IndicatorType> findSortByIndicatorCreatedOn(Pageable pageable);

	@Query("SELECT it FROM IndicatorType it INNER JOIN it.measure m WHERE m.type = :type")
	Page<IndicatorType> findByMeasure(Type type, Pageable pageable);

	@Query("SELECT it FROM IndicatorType it WHERE it.convertType = :convertType")
	Page<IndicatorType> findByConvertType(ConvertType convertType, Pageable pageable);

	@Query("DELETE FROM IndicatorType it WHERE it.id = :id")
	@Modifying
	void deleteById(Long id);

	@Query("SELECT EXISTS (SELECT it FROM IndicatorType it WHERE it.name = :name)")
	boolean existsByName(String name);

	@Query("SELECT EXISTS (SELECT it FROM IndicatorType it WHERE it.id = :id AND it.creator = :creator)")
	boolean existsByIdAndCreator(Long id, Long creator);
}
