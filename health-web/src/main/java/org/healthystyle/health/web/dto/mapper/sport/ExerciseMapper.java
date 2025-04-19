package org.healthystyle.health.web.dto.mapper.sport;

import org.healthystyle.health.model.sport.Exercise;
import org.healthystyle.health.web.dto.sport.ExerciseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = StepMapper.class)
public interface ExerciseMapper {
	ExerciseDto toDto(Exercise exercise);
}
