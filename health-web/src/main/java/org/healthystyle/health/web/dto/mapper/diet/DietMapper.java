package org.healthystyle.health.web.dto.mapper.diet;

import org.healthystyle.health.model.diet.Diet;
import org.healthystyle.health.web.dto.diet.DietDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DietMapper {
	DietDto toDto(Diet diet);
}
