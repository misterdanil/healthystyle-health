package org.healthystyle.health.web.measure.dto.mapper;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.web.measure.dto.MeasureDto;
import org.healthystyle.health.web.measure.dto.MeasureTranslater;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MeasureMapper {
	@Autowired
	private MeasureTranslater translater;

	@Mapping(source = "type", target = "translate", qualifiedByName = "getTranslateFromType")
	public abstract MeasureDto toDto(Measure measure);

	@Named("getTranslateFromType")
	String getTranslateFromType(Type type) {
		return translater.translate(type);
	}
}
