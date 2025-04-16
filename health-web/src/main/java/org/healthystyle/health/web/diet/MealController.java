package org.healthystyle.health.web.diet;

import java.util.List;

import org.healthystyle.health.model.diet.Meal;
import org.healthystyle.health.service.diet.MealService;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.dto.mapper.diet.MealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MealController {
	@Autowired
	private MealService service;
	@Autowired
	private MealMapper mapper;

	@GetMapping("/diets/{dietId}/meals")
	public ResponseEntity<?> getByDiet(@PathVariable Long dietId, @RequestParam Integer day, @RequestParam int page,
			@RequestParam int limit) {
		Page<Meal> meals;
		try {
			meals = service.findByDiet(dietId, day, page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(meals.map(mapper::toDto));
	}

	@GetMapping(value = "/meals", params = "planned")
	public ResponseEntity<?> getPlannedMeals(@RequestParam int page, @RequestParam int limit) {
		List<Meal> meals;
		try {
			meals = service.findPlanned(page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(meals.stream().map(mapper::toDto));
	}
	
	@GetMapping(value = "/meals", params = "next")
	public ResponseEntity<?> getNextMeals(@RequestParam int page, @RequestParam int limit) {
		List<Meal> meals;
		try {
			meals = service.findNextMeal(page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(meals.stream().map(mapper::toDto));
	}
}
