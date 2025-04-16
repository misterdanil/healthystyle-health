package org.healthystyle.health.repository.diet;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.model.diet.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long>, CustomMealRepository {
	@Query("SELECT m FROM Meal m INNER JOIN m.diet d WHERE d.id = :dietId AND m.day = :day AND m.time = :time")
	Meal findByDietAndDayAndTime(Long dietId, Integer day, LocalTime time);

	@Query("SELECT m FROM Meal m INNER JOIN m.foods f WHERE f.id IN :foodIds")
	Page<Meal> findByFoods(List<Long> foodIds, Long healthId, Pageable pageable);

	@Query(value = "SELECT m FROM meal m INNER JOIN diet d ON d.id = m.diet_id WHERE CAST(:date AS date) BETWEEN d.start AND d.end AND m.day = extract(isodow FROM :date) AND d.health_id = :healthId ORDER BY m.day, m.time OFFSET (:page - 1) * :limit LIMIT :limit", nativeQuery = true)
	List<Meal> findByDate(Instant date, Long healthId, int page, int limit);

	@Query("SELECT m FROM Meal m INNER JOIN m.diet d INNER JOIN d.health h WHERE CURRENT_DATE BETWEEN d.start AND d.end AND m.day = :day AND h.id = :healthId ORDER BY m.time")
	Page<Meal> findByDay(Integer day, Long healthId, Pageable pageable);

	@Query(value = "SELECT m.*, d.title AS diet_title FROM meal m INNER JOIN diet d ON d.id = m.diet_id WHERE d.finish >= CAST(CURRENT_TIMESTAMP AS DATE) AND d.health_id = :healthId ORDER BY m.day >= extract(isodow FROM CURRENT_TIMESTAMP) DESC, m.day, m.time >= CURRENT_TIME DESC, m.time OFFSET (:page - 1) * :limit LIMIT :limit", nativeQuery = true)
	List<Meal> findPlanned(Long healthId, int page, int limit);

	@Query(value = "WITH nextMealTime AS ("
			+ "SELECT m.time FROM meal m INNER JOIN diet d ON m.diet_id = d.id WHERE CURRENT_DATE BETWEEN d.start AND d.finish AND m.day = extract(isodow FROM CURRENT_DATE) AND m.time >= CURRENT_TIME AND d.health_id = ?1 ORDER BY m.time LIMIT 1"
			+ ")"
			+ "SELECT m.*, d.title as diet_title FROM meal m INNER JOIN diet d ON m.diet_id = d.id INNER JOIN nextMealTime nmt ON nmt.time = m.time WHERE CURRENT_DATE BETWEEN d.start AND d.finish AND m.day = extract(isodow FROM CURRENT_DATE) AND d.health_id = ?1 OFFSET (?2 - 1) * ?3 LIMIT ?3", nativeQuery = true)
	List<Meal> findNextMeal(Long healthId, int page, int limit);

	@Query("SELECT m FROM Meal m INNER JOIN m.diet d INNER JOIN d.health h WHERE d.id = :dietId AND m.day = :day AND h.id = :healthId")
	Page<Meal> findByDiet(Long dietId, Integer day, Long healthId, Pageable pageable);

	@Query("SELECT EXISTS (SELECT m FROM Meal m INNER JOIN m.diet d WHERE d.id = :dietId AND m.day = :day AND m.time = :time)")
	boolean existsByDietAndDayAndTime(Long dietId, Integer day, LocalTime time);

	@Query(value = "DELETE FROM meal m INNER JOIN meal_food mf ON mf.meal_id = m.id WHERE m.id = ?1 AND f.food_id IN ?2", nativeQuery = true)
	void deleteFoodsById(Long mealId, Set<Long> foodIds);

	@Query("DELETE FROM Meal m WHERE m.id IN :ids")
	void deleteByIds(Set<Long> ids);
}
