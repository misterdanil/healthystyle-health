package org.healthystyle.web;

import java.util.List;

import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.health.service.IndicatorTypeService;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.web.dto.IndicatorTypeDto;
import org.healthystyle.web.dto.mapper.IndicatorTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndicatorTypeConroller {
	@Autowired
	private IndicatorTypeService indicatorTypeService;
	@Autowired
	private IndicatorTypeMapper mapper;

	private static final Logger LOG = LoggerFactory.getLogger(IndicatorTypeConroller.class);

	@GetMapping("/metrics")
	public List<IndicatorTypeDto> getIndicatorTypes(@RequestParam Integer page, @RequestParam Integer limit)
			throws ValidationException {
		LOG.debug("Got request to get indicator types with page '{}' and limit '{}'", page, limit);
		Page<IndicatorType> metrics = indicatorTypeService.find(page, limit);

		return mapper.toDtos(metrics.toList());
	}
}
