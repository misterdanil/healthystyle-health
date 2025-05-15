package org.healthystyle.health.web.medicine;

import java.net.URI;
import java.net.URISyntaxException;

import org.healthystyle.health.model.medicine.Result;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.medicine.IntakeNotFoundException;
import org.healthystyle.health.service.error.medicine.ResultExistException;
import org.healthystyle.health.service.medicine.ResultService;
import org.healthystyle.health.service.medicine.dto.ResultSaveRequest;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("intake_result_controller")
public class ResultController {
	@Autowired
	private ResultService service;

	@PostMapping("/intakes/{id}/result")
	public ResponseEntity<?> add(@RequestBody ResultSaveRequest saveRequest, @PathVariable Long id)
			throws URISyntaxException {
		Result result;
		try {
			result = service.save(saveRequest, id);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (ResultExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (IntakeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.created(new URI("/intakeresults/" + result.getId())).build();
	}
}
