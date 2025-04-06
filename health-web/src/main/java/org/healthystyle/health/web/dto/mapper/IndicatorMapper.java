package org.healthystyle.health.web.dto.mapper;

import java.util.List;

import org.healthystyle.health.model.Indicator;
import org.healthystyle.health.web.dto.IndicatorDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IndicatorMapper {
	IndicatorDto toDto(Indicator indicator);

	List<IndicatorDto> toDtos(List<Indicator> indicator);
}
