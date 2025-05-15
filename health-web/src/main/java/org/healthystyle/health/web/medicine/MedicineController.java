package org.healthystyle.health.web.medicine;

import java.net.URI;
import java.net.URISyntaxException;

import org.healthystyle.health.model.medicine.Medicine;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.medicine.MedicineExistException;
import org.healthystyle.health.service.error.medicine.MedicineNotFoundException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.medicine.MedicineService;
import org.healthystyle.health.service.medicine.dto.MedicineSaveRequest;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.medicine.dto.mapper.MedicineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedicineController {
	@Autowired
	private MedicineService service;
	@Autowired
	private MedicineMapper mapper;

	@PostMapping("/medicine")
	public ResponseEntity<?> add(@RequestBody MedicineSaveRequest saveRequest) throws URISyntaxException {
		Medicine medicine;
		try {
			medicine = service.save(saveRequest);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (MedicineExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (WeightNegativeOrZeroException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (ConvertTypeNotRecognizedException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (MeasureNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}
		
		return ResponseEntity.created(new URI("/medicines/" + medicine.getId())).build();
	}

	@GetMapping("/medicines")
	public ResponseEntity<?> getByName(@RequestParam String name, @RequestParam int page, @RequestParam int limit)
			throws URISyntaxException {
		Page<Medicine> medicines;
		try {
			medicines = service.findByName(name, page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(medicines.map(mapper::toDto));
	}

	@DeleteMapping("/medicines/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) throws URISyntaxException {
		try {
			service.deleteById(id);
		} catch (MedicineNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.noContent().build();
	}
}