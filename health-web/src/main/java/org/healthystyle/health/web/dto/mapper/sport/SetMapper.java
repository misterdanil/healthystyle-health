package org.healthystyle.health.web.dto.mapper.sport;

import java.util.List;

import org.healthystyle.health.model.sport.Set;
import org.healthystyle.health.web.dto.sport.SetDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SetMapper {
	SetDto toDto(Set set);
	
	List<SetDto> toDtos(List<Set> set);
}
