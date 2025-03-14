package org.healthystyle.health.service.medicine;

import org.healthystyle.health.model.medicine.Medicine;
import org.healthystyle.health.service.error.medicine.MedicineNotFoundException;

public interface MedicineService {
	Medicine findById(Long id) throws MedicineNotFoundException;
}
