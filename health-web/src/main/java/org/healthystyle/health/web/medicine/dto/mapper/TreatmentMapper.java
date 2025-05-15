package org.healthystyle.health.web.medicine.dto.mapper;

import org.healthystyle.health.model.medicine.Treatment;
import org.healthystyle.health.web.medicine.dto.TreatmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TreatmentMapper {
	TreatmentDto toDto(Treatment treatment);
}
