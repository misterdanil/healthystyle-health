package org.healthystyle.health.web.dto.diet;

import java.time.LocalTime;
import java.util.List;

public class MealDto {
	private Long id;
	private Integer day;
	private LocalTime time;
	private List<MealFoodDto> foods;
	private DietDto diet;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public List<MealFoodDto> getFoods() {
		return foods;
	}

	public void setFoods(List<MealFoodDto> foods) {
		this.foods = foods;
	}

	public DietDto getDiet() {
		return diet;
	}

	public void setDiet(DietDto diet) {
		this.diet = diet;
	}

}
