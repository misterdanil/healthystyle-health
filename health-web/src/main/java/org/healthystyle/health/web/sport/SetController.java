package org.healthystyle.health.web.sport;

import org.healthystyle.health.model.sport.Set;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.sport.SetService;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.dto.mapper.sport.SetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SetController {
	@Autowired
	private SetService service;
	@Autowired
	private SetMapper mapper;

	private static final Logger LOG = LoggerFactory.getLogger(SetController.class);

	@GetMapping("/trains/{trainId}/sets")
	public ResponseEntity<?> getSetsByTrain(@PathVariable Long trainId, @RequestParam int page,
			@RequestParam int limit) {
		Page<Set> sets;
		try {
			sets = service.findByTrain(trainId, page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(sets.map(mapper::toDto));
	}
}
