package org.healthystyle.health.repository.diet.impl;

import java.time.LocalDate;
import java.util.List;

import org.healthystyle.health.repository.diet.CustomFoodValueRepository;
import org.healthystyle.health.repository.result.AvgStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class CustomFoodValueRepositoryImpl implements CustomFoodValueRepository {
	@Autowired
	private EntityManager em;

	@Override
	public Page<AvgStatistic> testFindAvgRangeWeek(Long nutritionValueId, Long healthId, LocalDate start, LocalDate to,
			Pageable pageable) {
		Query q = em.createNativeQuery(
				"SELECT DATE_TRUNC('week', CASE WHEN m.day IS NOT NULL THEN weeks + interval '1' day * m.day ELSE weeks END) AS week, TO_CHAR(AVG(CASE WHEN ct.dtype = 'int_numb' THEN CAST(fv.value AS INTEGER) WHEN ct.dtype = 'float_numb' THEN CAST(fv.value AS FLOAT) END), 'FM999999990.09') AS avgValue FROM GENERATE_SERIES(?3, ?4, interval '1 week') AS weeks LEFT JOIN diet d ON (d.start, d.finish) overlaps (weeks, interval '1 week') INNER JOIN meal m ON m.diet_id = d.id AND d.start <= (weeks + interval '1' day * m.day) AND d.finish >= (weeks + interval '1' day * m.day) INNER JOIN meal_food mf ON mf.meal_id = m.id INNER JOIN food f ON f.id = mf.food_id INNER JOIN food_value fv ON fv.food_id = f.id INNER JOIN nutrition_value nv ON nv.id = fv.nutrition_value_id INNER JOIN convert_type ct ON nv.convert_type_id = ct.id WHERE nv.id = ?1 AND d.health_id = ?2 GROUP BY date_trunc('week', CASE WHEN m.day IS NOT NULL THEN weeks + interval '1' day * m.day ELSE weeks END) ORDER BY date_trunc('week', case when m.day is not null then weeks + interval '1' day * m.day else weeks end)");
		q.setParameter(1, nutritionValueId);
		q.setParameter(2, healthId);
		q.setParameter(3, start);
		q.setParameter(4, to);
		List l = q.getResultList();
		return null;
	}

}
