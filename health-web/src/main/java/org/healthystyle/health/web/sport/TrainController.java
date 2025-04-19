package org.healthystyle.health.web.sport;

import java.util.Set;

import org.healthystyle.health.model.sport.Train;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.sport.TrainService;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.dto.mapper.sport.TrainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrainController {
	@Autowired
	private TrainService service;
	@Autowired
	private TrainMapper mapper;
	
	@GetMapping(value = "/sports/{sportId}/trains")
	public ResponseEntity<?> getTrainsBySport(@PathVariable Long sportId, @RequestParam int page, @RequestParam int limit) {
		try {
			Page<Train> trains = service.findBySport(sportId, page, limit);
			return ResponseEntity.ok(trains.map(mapper::toDto));
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}
	}

	@GetMapping(value = "/trains", params = "planned")
	public ResponseEntity<?> getPlanned(@RequestParam int page, @RequestParam int limit) {
		try {
			Set<Train> trains = service.findPlanned(page, limit);
			return ResponseEntity.ok(mapper.toDtos(trains));
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}
	}
}
