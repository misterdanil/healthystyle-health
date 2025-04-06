package org.healthystyle.health.web;

import java.util.List;

import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.health.service.IndicatorTypeService;
import org.healthystyle.health.service.dto.IndicatorTypeSort;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.web.dto.IndicatorTypeDto;
import org.healthystyle.health.web.dto.mapper.IndicatorTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndicatorTypeConroller {
	@Autowired
	private IndicatorTypeService indicatorTypeService;
	@Autowired
	private IndicatorTypeMapper mapper;

	private static final Logger LOG = LoggerFactory.getLogger(IndicatorTypeConroller.class);

	@GetMapping("/metrics")
	public List<IndicatorTypeDto> getIndicatorTypes(@RequestParam Integer page,
			@RequestParam(defaultValue = "NAME") Integer limit, IndicatorTypeSort sort) throws ValidationException {
		LOG.debug("Got request to get indicator types with page '{}' and limit '{}'", page, limit);
		Page<IndicatorType> metrics = indicatorTypeService.find(page, limit, sort);

		return mapper.toDtos(metrics.toList());
	}

	@GetMapping(value = "/metrics", params = "name")
	public List<IndicatorTypeDto> getIndicatorTypesByName(@RequestParam String name, @RequestParam Integer page,
			@RequestParam(defaultValue = "NAME") Integer limit, IndicatorTypeSort sort) throws ValidationException {
		LOG.debug("Got request to get indicator types with name '{}', page '{}' and limit '{}'", name, page, limit);
		Page<IndicatorType> metrics = indicatorTypeService.findByName(name, page, limit);

		return mapper.toDtos(metrics.toList());
	}
}
