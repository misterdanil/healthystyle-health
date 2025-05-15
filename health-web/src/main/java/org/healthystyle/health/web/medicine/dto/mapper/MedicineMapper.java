package org.healthystyle.health.web.medicine.dto.mapper;

import org.healthystyle.health.model.medicine.Medicine;
import org.healthystyle.health.web.measure.dto.mapper.MeasureMapper;
import org.healthystyle.health.web.medicine.dto.MedicineDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = MeasureMapper.class)
public interface MedicineMapper {
	MedicineDto toDto(Medicine medicine);
}
