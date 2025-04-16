package org.healthystyle.health.web.diet;

import java.time.LocalDate;

import org.healthystyle.health.repository.result.AvgStatistic;
import org.healthystyle.health.service.diet.FoodValueService;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.web.Range;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodValueController {
	@Autowired
	private FoodValueService service;

	@GetMapping(value = "/nutritions/{nutritionValueId}/values", params = "statistic")
	public ResponseEntity<?> getAvgStatistic(@PathVariable Long nutritionValueId,
			@DateTimeFormat(pattern = "YYYY-MM-DD") @RequestParam LocalDate start,
			@DateTimeFormat(pattern = "YYYY-MM-DD") @RequestParam LocalDate end, @RequestParam int page,
			@RequestParam int limit, @RequestParam Range range) {
		Page<AvgStatistic> statistic = null;
		try {
			if (range.equals(Range.WEEK)) {
				statistic = service.findAvgRangeWeek(nutritionValueId, start, end, page, limit);
			} else if (range.equals(Range.MONTH)) {
				statistic = service.findAvgRangeMonth(nutritionValueId, start, end, page, limit);
			} else {
				statistic = service.findAvgRangeYear(nutritionValueId, start, end, page, limit);
			}
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(statistic);
	}
}
