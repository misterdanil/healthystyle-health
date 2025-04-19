package org.healthystyle.health.web.sport;

import java.net.URI;
import java.net.URISyntaxException;

import org.healthystyle.health.model.sport.Exercise;
import org.healthystyle.health.service.dto.sport.ExerciseSaveRequest;
import org.healthystyle.health.service.dto.sport.ExerciseSort;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseExistException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.sport.ExerciseService;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.dto.mapper.sport.ExerciseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExerciseController {
	@Autowired
	private ExerciseService service;
	@Autowired
	private ExerciseMapper mapper;

	private static final Logger LOG = LoggerFactory.getLogger(ExerciseController.class);

	@PostMapping("/exercise")
	public ResponseEntity<?> addExercise(@RequestBody ExerciseSaveRequest saveRequest) throws URISyntaxException {
		LOG.debug("Got request to save exercise: {}", saveRequest);

		Exercise exercise;
		try {
			exercise = service.save(saveRequest);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (ExerciseExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (ExerciseNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.created(new URI("exercise/" + exercise.getId())).build();
	}

	@GetMapping("/exercises")
	public ResponseEntity<?> getExercises(@RequestParam String title, @RequestParam int page, @RequestParam int limit,
			@RequestParam ExerciseSort sort) {
		LOG.debug("Got request to get exercises");

		Page<Exercise> exercises;
		try {
			exercises = service.findByTitle(title, page, limit, sort);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(exercises.map(mapper::toDto));

	}
}
