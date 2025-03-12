package org.healthystyle.health.service.measure.convert;

import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.convert.ConvertTypeNotFoundException;

public interface ConvertTypeService {
	ConvertType findById(Long id) throws ConvertTypeNotFoundException;

	ConvertType getConvertTypeByValue(String value) throws ConvertTypeNotRecognizedException;
}
