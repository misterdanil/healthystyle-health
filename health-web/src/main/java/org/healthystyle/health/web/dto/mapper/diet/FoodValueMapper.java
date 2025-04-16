package org.healthystyle.health.web.dto.mapper.diet;

import java.util.List;

import org.healthystyle.health.model.diet.FoodValue;
import org.healthystyle.health.web.dto.diet.FoodValueDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FoodValueMapper {
	FoodValueDto toDto(FoodValue foodValue);
	
	List<FoodValueDto> toDtos(List<FoodValue> foodValues);

}
