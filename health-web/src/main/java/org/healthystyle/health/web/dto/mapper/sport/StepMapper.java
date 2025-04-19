package org.healthystyle.health.web.dto.mapper.sport;

import java.util.List;

import org.healthystyle.health.model.sport.Step;
import org.healthystyle.health.web.dto.sport.StepDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StepMapper {
	StepDto toDto(Step step);

	List<StepDto> toDtos(List<Step> steps);
}
