package org.healthystyle.health.web.measure;

import java.util.List;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.service.measure.MeasureService;
import org.healthystyle.health.web.measure.dto.mapper.MeasureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeasureController {
	@Autowired
	private MeasureService service;
	@Autowired
	private MeasureMapper mapper;

	@GetMapping("/measures")
	public ResponseEntity<?> getMeasures() {
		List<Measure> measures = service.findAll();

		return ResponseEntity.ok(measures.stream().map(mapper::toDto));
	}
}
