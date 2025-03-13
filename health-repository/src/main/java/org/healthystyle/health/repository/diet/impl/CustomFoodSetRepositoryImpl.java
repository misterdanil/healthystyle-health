package org.healthystyle.health.repository.diet.impl;

import java.util.List;

import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.repository.diet.CustomFoodSetRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CustomFoodSetRepositoryImpl implements CustomFoodSetRepository {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public void addFoods(List<Food> foods, Long foodSetId) {
		System.out.println("here");
		String sql = "INSERT INTO food_set_food (food_set_id, food_id) values (:food_set_id, :food_id)";

		MapSqlParameterSource[] params = foods.stream().map(food -> {
			MapSqlParameterSource s = new MapSqlParameterSource();
			s.addValue("food_set_id", foodSetId);
			s.addValue("food_id", food.getId());
			return s;
		}).toArray(MapSqlParameterSource[]::new);

		jdbcTemplate.batchUpdate(sql, params);
		
	}
	
	
}
