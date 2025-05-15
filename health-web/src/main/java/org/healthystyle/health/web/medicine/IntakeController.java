package org.healthystyle.health.web.medicine;

import java.util.List;

import org.healthystyle.health.model.medicine.Intake;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.medicine.IntakeService;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.medicine.dto.mapper.IntakeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IntakeController {
	@Autowired
	private IntakeService service;
	@Autowired
	private IntakeMapper mapper;

	@GetMapping(value = "/intakes", params = "planned")
	public ResponseEntity<?> getPlannedIntakes(@RequestParam int page, @RequestParam int limit) {
		List<Intake> intakes;
		try {
			intakes = service.findPlanned(page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(intakes.stream().map(mapper::toDto));
	}

	@GetMapping(value = "/intakes", params = "next")
	public ResponseEntity<?> getNextIntakes(@RequestParam int page, @RequestParam int limit) {
		List<Intake> intakes;
		try {
			intakes = service.findNextIntake(page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(intakes.stream().map(mapper::toDto));
	}

	@GetMapping(value = "/intakes", params = "missed")
	public ResponseEntity<?> getMissedIntakes(@RequestParam int page, @RequestParam int limit) {
		List<Intake> intakes;
		try {
			intakes = service.findNotExecuted(page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(intakes.stream().map(mapper::toDto));
	}
}
