package org.healthystyle.health.service;

import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.service.dto.IndicatorTypeSaveRequest;
import org.healthystyle.health.service.dto.IndicatorTypeUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.indicator.IndicatorTypeNotFoundException;
import org.healthystyle.health.service.error.indicator.NameExistedException;
import org.healthystyle.health.service.error.indicator.NotCreatorException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.measure.convert.ConvertTypeNotFoundException;
import org.springframework.data.domain.Page;

public interface IndicatorTypeService {
	IndicatorType findById(Long id) throws IndicatorTypeNotFoundException;

	Page<IndicatorType> findByName(String name, int page, int limit) throws ValidationException;

	Page<IndicatorType> find(int page, int limit) throws ValidationException;

	IndicatorType save(IndicatorTypeSaveRequest saveRequest)
			throws ValidationException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException;

	void deleteById(Long id) throws NotCreatorException;

	void update(IndicatorTypeUpdateRequest updateRequest) throws ValidationException, IndicatorTypeNotFoundException,
			NotCreatorException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException;

}
