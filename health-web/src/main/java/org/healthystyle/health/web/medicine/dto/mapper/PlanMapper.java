package org.healthystyle.health.web.medicine.dto.mapper;

import org.healthystyle.health.model.medicine.Plan;
import org.healthystyle.health.web.medicine.dto.PlanDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = TreatmentMapper.class)
public interface PlanMapper {
	PlanDto toDto(Plan plan);
}
