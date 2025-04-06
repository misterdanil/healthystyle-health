package org.healthystyle.health.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import org.healthystyle.health.model.Indicator;
import org.healthystyle.health.service.IndicatorService;
import org.healthystyle.health.service.dto.IndicatorSaveRequest;
import org.healthystyle.health.service.dto.IndicatorSort;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.indicator.IndicatorTypeNotFoundException;
import org.healthystyle.health.service.error.indicator.NotConvertableException;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.dto.mapper.IndicatorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndicatorController {
	@Autowired
	private IndicatorService service;
	@Autowired
	private IndicatorMapper mapper;

	@PostMapping("/metrics/{metricId}/value")
	public ResponseEntity<?> addIndicator(@RequestBody IndicatorSaveRequest saveRequest) throws URISyntaxException {
		try {
			Indicator indicator = service.save(saveRequest);
			return ResponseEntity.created(new URI("/indicators/" + indicator.getId())).build();
		} catch (ValidationException | NotConvertableException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (IndicatorTypeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}
	}

	@GetMapping("/indicators")
	public ResponseEntity<?> getIndicators(@RequestParam int page, @RequestParam int limit,
			@RequestParam IndicatorSort sort, Direction direction) {
		Page<Indicator> indicators;
		try {
			indicators = service.find(page, limit, sort, direction);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(indicators.map(mapper::toDto));
	}

	@GetMapping("/metrics/{metricId}/indicators")
	public ResponseEntity<?> getIndicators(@PathVariable Long metricId, @RequestParam int page, @RequestParam int limit,
			@RequestParam IndicatorSort sort, Direction direction) {
		Page<Indicator> indicators;
		try {
			indicators = service.findByIndicatorType(metricId, page, limit, sort, direction);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}
		
		return ResponseEntity.ok(indicators.map(mapper::toDto));
	}

	@GetMapping(value = "/metrics/{metricId}/indicators", params = "range")
	public ResponseEntity<?> getIndicatorsByRange(@PathVariable Long metricId, @RequestParam int page,
			@RequestParam int limit, @RequestParam(defaultValue = "NONE") Range range,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		Page<?> indicators;
		try {
			if (range.equals(Range.WEEK)) {
				indicators = service.findChangesByIndicatorTypeWeeksRange(metricId, start, end, page, limit);
			} else if (range.equals(Range.MONTH)) {
				indicators = service.findChangedByIndicatorTypeMonthsRange(metricId, start, end, page, limit);
			} else if (range.equals(Range.YEAR)) {
				indicators = service.findChangesByIndicatorTypeYearsRange(metricId, start, end, page, limit);
			} else {
				indicators = service.findChangesByIndicatorType(metricId, start, end, page, limit);
			}
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(indicators);
	}

	public static void main(String[] args) {
		System.out.println(IndicatorSort.valueOf("DATE"));
	}
}
