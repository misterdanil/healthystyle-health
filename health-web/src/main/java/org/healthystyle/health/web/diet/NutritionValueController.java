package org.healthystyle.health.web.diet;

import org.healthystyle.health.model.diet.NutritionValue;
import org.healthystyle.health.service.diet.NutritionValueService;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.dto.mapper.diet.NutritionValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NutritionValueController {
	@Autowired
	private NutritionValueService service;
	@Autowired
	private NutritionValueMapper mapper;

	private static final Logger LOG = LoggerFactory.getLogger(NutritionValueController.class);

	@GetMapping("/values")
	public ResponseEntity<?> getNutritionValues(@RequestParam int page, @RequestParam int limit) {
		LOG.debug("Getting nutrition values by page '{}' and limit '{}'", page, limit);

		Page<NutritionValue> nutritionValues;
		try {
			nutritionValues = service.find(page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(nutritionValues.map(mapper::toDto));
	}
	
	@GetMapping(value = "/values", params = "value")
	public ResponseEntity<?> getNutritionByValue(@RequestParam String value, @RequestParam int page, @RequestParam int limit) {
		LOG.debug("Getting nutrition values by page '{}' and limit '{}'", page, limit);

		Page<NutritionValue> nutritionValues;
		try {
			nutritionValues = service.findByValue(value, page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(nutritionValues.map(mapper::toDto));
	}
}
