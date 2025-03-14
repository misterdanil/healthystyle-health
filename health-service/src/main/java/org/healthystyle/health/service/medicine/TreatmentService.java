package org.healthystyle.health.service.medicine;

import org.healthystyle.health.model.medicine.Treatment;
import org.healthystyle.health.service.error.medicine.TreatmentNotFoundException;

public interface TreatmentService {
	Treatment findById(Long id) throws TreatmentNotFoundException;
}
