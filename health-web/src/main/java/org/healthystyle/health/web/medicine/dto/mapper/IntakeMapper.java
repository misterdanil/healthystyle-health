package org.healthystyle.health.web.medicine.dto.mapper;

import org.healthystyle.health.model.medicine.Intake;
import org.healthystyle.health.web.measure.dto.mapper.MeasureMapper;
import org.healthystyle.health.web.medicine.dto.IntakeDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { PlanMapper.class, MeasureMapper.class })
public interface IntakeMapper {
	IntakeDto toDto(Intake intake);
}
