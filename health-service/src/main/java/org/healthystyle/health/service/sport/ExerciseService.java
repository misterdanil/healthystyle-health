package org.healthystyle.health.service.sport;

import org.healthystyle.health.model.sport.Exerсise;
import org.springframework.data.domain.Page;

public interface ExerciseService {
	Page<Exerсise> findByTitle(String title, int page, int limit);
	
	Page<Exerсise> find(Long healthId, int page, int limit);
	
	Exercise save(ExerciseSaveRequest saveRequest);
	
	void update(ExerciseUpdateRequest updateRequest);
	
}
