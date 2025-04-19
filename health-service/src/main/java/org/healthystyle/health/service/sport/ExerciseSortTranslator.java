package org.healthystyle.health.service.sport;

import org.healthystyle.health.service.SortTranslator;
import org.healthystyle.health.service.dto.sport.ExerciseSort;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class ExerciseSortTranslator implements SortTranslator<ExerciseSort> {

	@Override
	public Sort translateToSort(ExerciseSort sort) {
		if (sort.equals(ExerciseSort.TITLE)) {
			return Sort.by(Direction.ASC, "title");
		} else if (sort.equals(ExerciseSort.CREATED_ON)) {
			return Sort.by(Direction.DESC, "createdOn");
		} else {
			return null;
		}
	}

}
