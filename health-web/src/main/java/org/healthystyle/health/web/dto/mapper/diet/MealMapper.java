package org.healthystyle.health.web.dto.mapper.diet;

import java.util.List;

import org.healthystyle.health.model.diet.Meal;
import org.healthystyle.health.web.dto.diet.MealDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MealMapper {
	MealDto toDto(Meal meal);

	List<MealDto> toDtos(List<Meal> meals);

}
