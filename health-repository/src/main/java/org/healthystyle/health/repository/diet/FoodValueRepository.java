package org.healthystyle.health.repository.diet;

import java.time.Instant;
import java.util.List;

import org.healthystyle.health.model.diet.FoodValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodValueRepository extends JpaRepository<FoodValue, Long> {
	@Query("SELECT fv FROM FoodValue fv INNER JOIN fv.foods f INNER JOIN f.health h WHERE f.id = :foodId AND h.id = :healthId")
	Page<FoodValue> findByFood(Long foodId, Long healthId, Pageable pageable);

	@Query("SELECT fv FROM FoodValue fv INNER JOIN fv.nutritionValue nv INNER JOIN nv.convertType ct WHERE nv.id = :nutritionValueId AND (TYPE(ct) = IntegerNumber AND CAST(fv.value AS integer) <= CAST(:value AS integer) OR TYPE(ct) = FloatNumber AND CAST(fv.value AS numeric) <= CAST(:value AS numeric))")
	Page<FoodValue> findByValueAndNutritionValue(String value, Long nutritionValueId, Pageable pageable);

	@Query("SELECT SUM(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(value AS INTEGER) WHEN TYPE(ct) = FloatNumber) THEN CAST(value AS FLOAT) END) FROM FoodValue fv INNER JOIN fv.nutritionValue nv INNER JOIN nv.convertType ct INNER JOIN fv.food f WHERE nv.id = :nutritionValueId AND f.id IN :foodIds")
	Integer findSumByFoodsAndNutritionValue(List<Long> foodIds, Long nutritionValueId);

	@Query(value = "SELECT DATE_TRUNC('week', CASE WHEN m.day IS NOT NULL THEN weeks + interval '1' day * m.day ELSE weeks END) AS week, AVG(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(value AS INTEGER) WHEN TYPE(ct) = FloatNumber) THEN CAST(value AS FLOAT) END) FROM GENERATE_SERIES(?2::date, ?3::date, interval '1' week) AS weeks LEFT JOIN diet d ON (d.start, d.end) overlaps (weeks, interval '1 week') INNER JOIN health h ON h.id = d.health_id INNER JOIN meal m ON m.diet_id = h.id AND d.start <= (weeks + interval '1' day * m.day) AND d.end >= (weeks + interval '1' day * m.day) INNER JOIN meal_food mf ON mf.meal_id = m.id INNER JOIN food f ON f.id = mf.food_id INNER JOIN food_value fv ON fv.food_id = f.id INNER JOIN nutrition_value nv ON nv.id = fv.nutrition_value_id INNER JOIN convert_type ct ON nv.convert_type_id = ct.id WHERE nv.id = ?0 AND h.id = ?1 GROUP BY date_trunc('week', CASE WHEN m.day IS NOT NULL THEN weeks + interval '1' day * m.day ELSE weeks END) ORDER BY date_trunc('week', case when m.day is not null then weeks + interval '1' day * m.day else weeks end)", nativeQuery = true)
	Page<FoodValue> findAvgRangeWeek(Long nutritionValueId, Long healthId, Instant start, Instant to,
			Pageable pageable);

	@Query(value = "SELECT DATE_TRUNC('month', CASE WHEN m.day IS NOT NULL THEN weeks + interval '1' day * m.day ELSE weeks END) AS month, AVG(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(value AS INTEGER) WHEN TYPE(ct) = FloatNumber) THEN CAST(value AS FLOAT) END) FROM GENERATE_SERIES(?2::date, ?3::date, interval '1' week) AS weeks LEFT JOIN diet d ON (d.start, d.end) OVERLAPS (weeks, interval '1 week') INNER JOIN health h ON h.id = d.health_id INNER JOIN meal m ON m.diet_id = h.id AND d.start <= (weeks + interval '1' day * m.day) AND d.end >= (weeks + interval '1' day * m.day) INNER JOIN meal_food mf ON mf.meal_id = m.id INNER JOIN food f ON f.id = mf.food_id INNER JOIN food_value fv ON fv.food_id = f.id INNER JOIN nutrition_value nv ON nv.id = fv.nutrition_value_id INNER JOIN convert_type ct ON nv.convert_type_id = ct.id WHERE nv.id = ?0 AND h.id = ?1 AND date_trunc('month', case when m.day is not null then weeks + interval '1' day * m.day else weeks end) >= date_trunc('month', ?2::date) AND date_trunc('month', case when m.day is not null then weeks + interval '1' day * m.day else weeks end) <= date_trunc('month', ?3::date) GROUP BY date_trunc('month', CASE WHEN m.day IS NOT NULL THEN weeks + interval '1' day * m.day ELSE weeks END) ORDER BY date_trunc('month', case when m.day is not null then weeks + interval '1' day * m.day else weeks end)", nativeQuery = true)
	Page<FoodValue> findAvgRangeMonth(Long nutritionValueId, Long healthId, Instant start, Instant to,
			Pageable pageable);

	@Query(value = "SELECT DATE_TRUNC('year', CASE WHEN m.day IS NOT NULL THEN weeks + interval '1' day * m.day ELSE weeks END) AS year, AVG(CASE WHEN TYPE(ct) = IntegerNumber THEN CAST(value AS INTEGER) WHEN TYPE(ct) = FloatNumber) THEN CAST(value AS FLOAT) END) FROM GENERATE_SERIES(?2::date, ?3::date, interval '1' week) AS weeks LEFT JOIN diet d ON (d.start, d.end) OVERLAPS (weeks, interval '1 week') INNER JOIN health h ON h.id = d.health_id INNER JOIN meal m ON m.diet_id = h.id AND d.start <= (weeks + interval '1' day * m.day) AND d.end >= (weeks + interval '1' day * m.day) INNER JOIN meal_food mf ON mf.meal_id = m.id INNER JOIN food f ON f.id = mf.food_id INNER JOIN food_value fv ON fv.food_id = f.id INNER JOIN nutrition_value nv ON nv.id = fv.nutrition_value_id INNER JOIN convert_type ct ON nv.convert_type_id = ct.id WHERE nv.id = ?0 AND h.id = ?1 AND date_trunc('year', case when m.day is not null then weeks + interval '1' day * m.day else weeks end) >= date_trunc('month', ?2::date) AND date_trunc('year', case when m.day is not null then weeks + interval '1' day * m.day else weeks end) <= date_trunc('year', ?3::date) GROUP BY date_trunc('year', CASE WHEN m.day IS NOT NULL THEN weeks + interval '1' day * m.day ELSE weeks END) ORDER BY date_trunc('year', case when m.day is not null then weeks + interval '1' day * m.day else weeks end)", nativeQuery = true)
	Page<FoodValue> findAvgRangeYear(Long nutritionValueId, Long healthId, Instant start, Instant to,
			Pageable pageable);
}
