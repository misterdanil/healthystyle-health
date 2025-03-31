package org.healthystyle.web.dto.mapper;

import java.util.List;

import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.web.dto.IndicatorTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IndicatorTypeMapper {
	IndicatorTypeDto toDto(IndicatorType indicatorType);
	

	List<IndicatorTypeDto> toDtos(List<IndicatorType> indicatorTypes);
}
