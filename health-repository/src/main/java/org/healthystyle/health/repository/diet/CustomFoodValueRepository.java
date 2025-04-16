package org.healthystyle.health.repository.diet;

import java.time.LocalDate;

import org.healthystyle.health.repository.result.AvgStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomFoodValueRepository {
	Page<AvgStatistic> testFindAvgRangeWeek(Long nutritionValueId, Long healthId, LocalDate start, LocalDate to,
			Pageable pageable);

}
