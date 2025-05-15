package org.healthystyle.health.web.medicine;

import java.net.URI;
import java.net.URISyntaxException;

import org.healthystyle.health.model.medicine.Treatment;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.medicine.IntakeExistException;
import org.healthystyle.health.service.error.medicine.MedicineNotFoundException;
import org.healthystyle.health.service.error.medicine.PlanOverlapsException;
import org.healthystyle.health.service.error.medicine.TreatmentExistException;
import org.healthystyle.health.service.error.medicine.TreatmentNotFoundException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.medicine.TreatmentService;
import org.healthystyle.health.service.medicine.dto.TreatmentSaveRequest;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TreatmentController {
	@Autowired
	private TreatmentService service;

	@PostMapping("/treatment")
	public ResponseEntity<?> add(@RequestBody TreatmentSaveRequest saveRequest) throws URISyntaxException {
		Treatment treatment;
		try {
			treatment = service.save(saveRequest);
		} catch (MeasureNotFoundException | TreatmentNotFoundException | MedicineNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (ConvertTypeNotRecognizedException | WeightNegativeOrZeroException | ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (PlanOverlapsException | IntakeExistException | TreatmentExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.created(new URI("/treatments/" + treatment.getId())).build();
	}
}
