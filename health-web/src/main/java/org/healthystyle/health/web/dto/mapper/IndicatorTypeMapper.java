package org.healthystyle.health.web.dto.mapper;

import java.util.List;

import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.health.web.dto.IndicatorTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface IndicatorTypeMapper {
	IndicatorTypeDto toDto(IndicatorType indicatorType);

	List<IndicatorTypeDto> toDtos(List<IndicatorType> indicatorTypes);
}
