package org.healthystyle.health.web.dto.diet;

public class MealFoodDto {
	private Long id;
	private Float weight;
	private FoodDto food;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public FoodDto getFood() {
		return food;
	}

	public void setFood(FoodDto food) {
		this.food = food;
	}

}
