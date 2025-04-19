package org.healthystyle.health.web.sport;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.healthystyle.health.model.sport.Sport;
import org.healthystyle.health.service.dto.sport.SportSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.error.sport.SportNotFoundException;
import org.healthystyle.health.service.error.sport.TrainExistException;
import org.healthystyle.health.service.error.sport.TrainNotFoundException;
import org.healthystyle.health.service.sport.SportService;
import org.healthystyle.health.web.dto.ErrorResponse;
import org.healthystyle.health.web.dto.mapper.sport.SportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SportController {
	@Autowired
	private SportService service;
	@Autowired
	private SportMapper mapper;

	private static final Logger LOG = LoggerFactory.getLogger(SportController.class);

	@PostMapping("/sport")
	public ResponseEntity<?> addSport(@RequestBody SportSaveRequest saveRequest) throws URISyntaxException {
		LOG.debug("Got request to add sport: {}", saveRequest);

		Sport sport;
		try {
			sport = service.save(saveRequest);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (TrainExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		} catch (SportNotFoundException | TrainNotFoundException | ExerciseNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.created(new URI("/sport/" + sport.getId())).build();
	}
	
	@GetMapping(value = "/sports", params = "description")
	public ResponseEntity<?> getSportsByDescription(@RequestParam String description, @RequestParam int page, @RequestParam int limit) throws URISyntaxException {
		LOG.debug("Got request to get sports by description '{}'", description);

		Set<Sport> sports;
		try {
			sports = service.findByDescription(description, page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(mapper.toDtos(sports));
	}
}
