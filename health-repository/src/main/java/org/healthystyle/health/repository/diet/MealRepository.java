package org.healthystyle.health.repository.diet;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.healthystyle.health.model.diet.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository {
	@Query("SELECT m FROM Meal m INNER JOIN m.foods f WHERE f.id IN :foodIds")
	Page<Meal> findByFoods(List<Long> foodIds, Long healthId);

	@Query("SELECT m FROM Meal m INNER JOIN m.foodSet fs WHERE fs.id = :foodSetId")
	Page<Meal> findByFoodSet(Long foodSetId, Long healthId);

	@Query("SELECT m FROM Meal m INNER JOIN m.diet d WHERE CAST(:date AS date) BETWEEN d.start AND d.end AND m.day = extract(dow FROM :date) ORDER BY m.day, m.time")
	Page<Meal> findByDate(Instant date);

	@Query("SELECT m FROM Meal m INNER JOIN m.diet d WHERE CURRENT_DATE BETWEEN d.start AND d.end AND m.day = :day ORDER BY m.time")
	Page<Meal> findByDay(Integer day);

	@Query("SELECT m FROM Meal m INNER JOIN m.diet d WHERE d.end >= CAST(CURRENT_TIME AS DATE) ORDER BY d.start, m.day >= extract(dow FROM CURRENT_TIMESTAMP) DESC, m.day, m.time >= CURRENT_TIME DESC, m.time")
	Page<Meal> findPlanned();

	@Query(value = "WITH nextMealTime AS ("
			+ "SELECT m.time FROM meal m INNER JOIN diet d ON m.diet_id = d.id WHERE CURRENT_DATE BETWEEN d.start AND d.end AND m.day = extract(dow FROM CURRENT_DATE) AND m.time >= CURRENT_TIME ORDER BY m.time LIMIT 1)"
			+ ")"
			+ "SELECT m FROM meal m INNER JOIN diet d ON m.diet_id = d.id WHERE CURRENT_DATE BETWEEN d.start AND d.end AND m.day = extract(dow FROM CURRENT_DATE) AND m.time == nextMealTime")
	Page<Meal> findNextMeal();

	@Query("SELECT m FROM Meal m INNER JOIN m.diet d WHERE d.id = :dietId")
	Page<Meal> findByDiet(Long dietId);
}
