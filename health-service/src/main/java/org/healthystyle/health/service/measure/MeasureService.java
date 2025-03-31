package org.healthystyle.health.service.measure;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;

public interface MeasureService {
	Measure findByType(Type type) throws MeasureNotFoundException, ValidationException;
}
