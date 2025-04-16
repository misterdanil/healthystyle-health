package org.healthystyle.health.repository.diet;

import java.util.List;
import java.util.Set;

import org.healthystyle.health.model.diet.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
	@Query("SELECT f FROM Food f WHERE f.id IN :ids")
	List<Food> findByIds(Set<Long> ids);

	@Query("SELECT f FROM Food f INNER JOIN f.health h WHERE LOWER(f.title) LIKE CONCAT('%', LOWER(:title), '%') AND h.id = :healthId")
	Page<Food> findByTitle(String title, Long healthId, Pageable pageable);

	@Query("SELECT f FROM Food f INNER JOIN f.health h WHERE h.id = :healthId ORDER BY f.createdOn DESC")
	Page<Food> find(Long healthId, Pageable pageable);

	@Query(value = "SELECT f FROM food f LEFT JOIN meal_food mf ON mf.food_id = f.id AND mf.meal_id = ?2 WHERE f.id IN ?1 AND mf.meal_id IS NULL", nativeQuery = true)
	List<Food> findByIdsExcludeMeal(Set<Long> ids, Long mealId);

	@Query("SELECT EXISTS (SELECT f FROM Food f INNER JOIN f.health h WHERE h.id = :healthId AND LOWER(f.title) = LOWER(:title))")
	boolean existsByTitle(String title, Long healthId);
}