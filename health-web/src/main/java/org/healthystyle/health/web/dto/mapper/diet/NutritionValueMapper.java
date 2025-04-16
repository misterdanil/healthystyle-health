package org.healthystyle.health.web.dto.mapper.diet;

import org.healthystyle.health.model.diet.NutritionValue;
import org.healthystyle.health.web.dto.diet.NutritionValueDto;
import org.healthystyle.health.web.dto.mapper.EnumToStringTranslator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = EnumToStringTranslator.class)
public interface NutritionValueMapper {
	@Mapping(source = "value", target = "translation", qualifiedByName = "enumToString")
	NutritionValueDto toDto(NutritionValue nutritionValue);
}
