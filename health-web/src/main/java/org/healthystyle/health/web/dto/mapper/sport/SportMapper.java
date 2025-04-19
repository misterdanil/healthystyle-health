package org.healthystyle.health.web.dto.mapper.sport;

import java.util.Set;

import org.healthystyle.health.model.sport.Sport;
import org.healthystyle.health.web.dto.sport.SportDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SportMapper {
	SportDto toDto(Sport sport);
	
	Set<SportDto> toDtos(Set<Sport> sports);

}
