package org.healthystyle.health.repository.diet.impl;

import java.util.List;

import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.repository.diet.CustomMealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CustomMealRepositoryImpl implements CustomMealRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public void addFoods(List<Food> foods, Long mealId) {
		System.out.println("here");
		String sql = "INSERT INTO meal_food (meal_id, food_id) values (:meal_id, :food_id)";

		MapSqlParameterSource[] params = foods.stream().map(food -> {
			MapSqlParameterSource s = new MapSqlParameterSource();
			s.addValue("meal_id", mealId);
			s.addValue("food_id", food.getId());
			return s;
		}).toArray(MapSqlParameterSource[]::new);

		jdbcTemplate.batchUpdate(sql, params);
	}

}
