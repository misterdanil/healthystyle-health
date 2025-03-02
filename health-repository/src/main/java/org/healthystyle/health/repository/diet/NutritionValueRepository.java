package org.healthystyle.health.repository.diet;

import org.healthystyle.health.model.diet.NutritionValue;
import org.healthystyle.health.model.diet.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionValueRepository {
	@Query("SELECT nv FROM NutritionValue nv WHERE nv.value = :value")
	NutritionValue findByValue(Value value);
}
