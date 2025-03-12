package org.healthystyle.health.repository.diet;

import org.healthystyle.health.model.diet.MealFood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MealFoodRepository extends JpaRepository<MealFood, Long> {
	@Query("SELECT mf FROM MealFood mf INNER JOIN mf.meal m WHERE m.id = :mealId")
	Page<MealFood> findByMeal(Long mealId, Pageable pageable);
}
