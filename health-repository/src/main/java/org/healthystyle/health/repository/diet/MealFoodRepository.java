package org.healthystyle.health.repository.diet;

import java.util.Set;

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

	@Query("SELECT EXISTS (SELECT mf FROM MealFood mf WHERE mf.meal = :mealId AND mf.food = :foodId)")
	boolean existsByMealAndFood(Long mealId, Long foodId);

	@Query("DELETE FROM MealFood mf WHERE mf.id IN :ids")
	void deleteByIds(Set<Long> ids);

	@Query("SELECT COUNT(mf) FROM MealFood mf WHERE mf.meal_id = :mealId")
	Integer countFoods(Long mealId);

}
