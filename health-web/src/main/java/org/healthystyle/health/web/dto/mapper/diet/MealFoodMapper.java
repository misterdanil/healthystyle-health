package org.healthystyle.health.web.dto.mapper.diet;

import java.util.List;

import org.healthystyle.health.model.diet.MealFood;
import org.healthystyle.health.web.dto.diet.MealFoodDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = FoodMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MealFoodMapper {
	MealFoodDto toDto(MealFood mealFood);

	List<MealFoodDto> toDtos(List<MealFood> mealFoods);
}
