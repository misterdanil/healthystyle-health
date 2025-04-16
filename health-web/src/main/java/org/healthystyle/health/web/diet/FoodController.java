package org.healthystyle.health.web.diet;

import java.net.URI;
import java.net.URISyntaxException;

import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.service.diet.FoodService;
import org.healthystyle.health.service.dto.diet.FoodSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodSort;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeMismatchException;
import org.healthystyle.health.service.error.diet.FoodExistException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodValueExistException;
import org.healthystyle.health.service.error.diet.NutritionValueNotFoundException;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.dto.mapper.diet.FoodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodController {
	@Autowired
	private FoodService service;
	@Autowired
	private FoodMapper mapper;

	private static final Logger LOG = LoggerFactory.getLogger(FoodController.class);

	@PostMapping("/food")
	public ResponseEntity<?> addFood(@RequestBody FoodSaveRequest saveRequest) throws URISyntaxException {
		LOG.debug("Got food to save: {}", saveRequest);

		Food food;
		try {
			food = service.save(saveRequest);
		} catch (ConvertTypeMismatchException | ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (FoodValueExistException | FoodExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (FoodNotFoundException | NutritionValueNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.created(new URI("food/" + food.getId())).build();
	}

	@GetMapping(value = "/foods", params = "title")
	public ResponseEntity<?> getFoodByTitle(String title, int page, int limit, FoodSort sort)
			throws URISyntaxException {
		LOG.debug("Got request to get foods");

		Page<Food> foods;
		try {
			foods = service.findByTitle(title, page, limit, sort);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(foods.map(mapper::toDto));
	}
}
