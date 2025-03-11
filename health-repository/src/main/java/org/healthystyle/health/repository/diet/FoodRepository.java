package org.healthystyle.health.repository.diet;

import java.util.List;
import java.util.Set;

import org.healthystyle.health.model.diet.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository {
	@Query("SELECT f FROM Food f INNER JOIN f.health h WHERE f.title LIKE '%:title%' AND h.id = :healthId ORDER BY f.createdOn")
	Page<Food> findByTitle(String title, Long healthId, Pageable pageable);

	@Query("SELECT f FROM Food f INNER JOIN f.health h WHERE h.id = :healthId ORDER BY f.createdOn DESC")
	Page<Food> find(Long healthId, Pageable pageable);

	@Query(value = "SELECT f FROM food f LEFT JOIN meal_food mf ON mf.food_id = f.id AND mf.meal_id = ?1 WHERE f.id IN ?0 AND mf.meal_id IS NULL")
	List<Food> findByIdsExcludeMeal(Set<Long> ids, Long mealId);
}
