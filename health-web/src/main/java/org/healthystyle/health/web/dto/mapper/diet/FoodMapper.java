package org.healthystyle.health.web.dto.mapper.diet;

import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.web.dto.diet.FoodDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = NutritionValueMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FoodMapper {
	FoodDto toDto(Food food);
}
