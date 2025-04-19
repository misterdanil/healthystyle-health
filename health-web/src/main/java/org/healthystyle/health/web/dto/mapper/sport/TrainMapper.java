package org.healthystyle.health.web.dto.mapper.sport;

import java.util.Set;

import org.healthystyle.health.model.sport.Train;
import org.healthystyle.health.web.dto.sport.TrainDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { SportMapper.class, SetMapper.class })
public interface TrainMapper {
	@Mapping(source = "sport.id", target = "sportId")
	@Mapping(source = "sport.description", target = "sportDescription")
	TrainDto toDto(Train train);

	Set<TrainDto> toDtos(Set<Train> train);
}
