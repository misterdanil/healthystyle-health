package org.healthystyle.health.repository.diet;

import java.util.List;

import org.healthystyle.health.model.diet.NutritionValue;
import org.healthystyle.health.model.diet.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionValueRepository extends JpaRepository<NutritionValue, Long> {
	@Query("SELECT nv FROM NutritionValue nv WHERE nv.value = :value")
	NutritionValue findByValue(Value value);

	@Query("SELECT nv FROM NutritionValue nv WHERE nv.value IN :values")
	Page<NutritionValue> findByValues(List<Value> values, Pageable pageable);
}
