package org.healthystyle.health.web.diet;

import java.net.URI;
import java.net.URISyntaxException;

import org.healthystyle.health.model.diet.Diet;
import org.healthystyle.health.service.diet.DietService;
import org.healthystyle.health.service.dto.diet.DietSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.DietExistException;
import org.healthystyle.health.service.error.diet.MealFoodNotFoundException;
import org.healthystyle.health.service.error.diet.MealSaveException;
import org.healthystyle.health.service.error.diet.MealTimeDuplicateException;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.dto.mapper.diet.DietMapper;
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
public class DietController {
	@Autowired
	private DietService service;
	@Autowired
	private DietMapper mapper;

	private static final Logger LOG = LoggerFactory.getLogger(DietController.class);

	@PostMapping("/diet")
	public ResponseEntity<?> addDiet(@RequestBody DietSaveRequest saveRequest) throws URISyntaxException {
		LOG.debug("Got diet request: {}", saveRequest);

		Diet diet;
		try {
			diet = service.save(saveRequest);
		} catch (MealSaveException | MealTimeDuplicateException | ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (DietExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (MealFoodNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.created(new URI("/diets/" + diet.getId())).build();
	}

	@GetMapping(value = "/diets", params = "title")
	public ResponseEntity<?> getDietsByTitle(@RequestParam String title, @RequestParam int page,
			@RequestParam int limit) throws URISyntaxException {
		LOG.debug("Got diet request");

		Page<Diet> diets;
		try {
			diets = service.findByTitle(title, page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(diets.map(mapper::toDto));
	}

	@GetMapping(value = "/diets", params = "actual")
	public ResponseEntity<?> getActualDiets(@RequestParam int page, @RequestParam int limit) throws URISyntaxException {
		LOG.debug("Got diet request");

		Page<Diet> diets;
		try {
			diets = service.findActual(page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(diets.map(mapper::toDto));
	}
}
