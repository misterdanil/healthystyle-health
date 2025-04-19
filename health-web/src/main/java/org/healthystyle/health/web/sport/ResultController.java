package org.healthystyle.health.web.sport;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import org.healthystyle.health.model.sport.Result;
import org.healthystyle.health.repository.result.DateStatistic;
import org.healthystyle.health.repository.result.TimeStatistic;
import org.healthystyle.health.service.dto.sport.ResultSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ResultExistException;
import org.healthystyle.health.service.error.sport.SetNotFoundException;
import org.healthystyle.health.service.sport.ResultService;
import org.healthystyle.health.web.Range;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class ResultController {
	@Autowired
	private ResultService service;

	private static final Logger LOG = LoggerFactory.getLogger(ResultController.class);

	@GetMapping("/sports/statistic")
	public ResponseEntity<?> getRangeResults(@DateTimeFormat(pattern = "YYYY-MM-DD") @RequestParam LocalDate start,
			@DateTimeFormat(pattern = "YYYY-MM-DD") @RequestParam LocalDate end, @RequestParam int page,
			@RequestParam int limit, @RequestParam Range range) {
		LOG.debug("Got request to get range results");

		Page<DateStatistic> statistic;
		try {
			if (range.equals(Range.DAY)) {
				statistic = service.findPercentageRangeDays(start, end, page, limit);
			} else if (range.equals(Range.WEEK)) {
				statistic = service.findPercentageRangeWeeks(start, end, page, limit);
			} else {
				statistic = service.findPercentageRangeMonths(start, end, page, limit);
			}
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(statistic);
	}

	@GetMapping(value = "/sports/statistic", params = "date")
	public ResponseEntity<?> getResultsByDate(@DateTimeFormat(pattern = "YYYY-MM-DD") @RequestParam LocalDate date,
			@RequestParam int page, @RequestParam int limit) {
		LOG.debug("Got request to get range results");

		Page<TimeStatistic> statistic;
		try {
			statistic = service.findPercentageByDate(date, page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(statistic);
	}

	@PostMapping(value = "/sets/{setId}/result")
	public ResponseEntity<?> addResult(@PathVariable Long setId, @RequestBody ResultSaveRequest saveRequest) throws URISyntaxException {
		LOG.debug("Got request to add set");

		Result result;
		try {
			result = service.save(saveRequest, setId);
		} catch (ResultExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (SetNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.created(new URI("/sets/" + setId + "/results/" + result.getId())).build();
	}
}
