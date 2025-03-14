package org.healthystyle.health.service.medicine;

import org.healthystyle.health.model.medicine.Medicine;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.medicine.MedicineExistException;
import org.healthystyle.health.service.error.medicine.MedicineNotFoundException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.healthystyle.health.service.medicine.dto.MedicineSaveRequest;
import org.healthystyle.health.service.medicine.dto.MedicineUpdateRequest;
import org.springframework.data.domain.Page;

public interface MedicineService {
	static final String[] FIND_BY_NAME_PARAM_NAMES = MethodNameHelper.getMethodParamNames(MedicineService.class,
			"findByName", String.class, int.class, int.class);

	static final String[] FIND_PARAM_NAMES = MethodNameHelper.getMethodParamNames(MedicineService.class, "find",
			int.class, int.class);

	Medicine findById(Long id) throws ValidationException, MedicineNotFoundException;

	Page<Medicine> findByName(String name, int page, int limit) throws ValidationException;

	Page<Medicine> find(int page, int limit) throws ValidationException;

	Medicine save(MedicineSaveRequest saveRequest) throws ValidationException, MedicineExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException;

	void update(MedicineUpdateRequest updateRequest, Long id) throws ValidationException, MedicineNotFoundException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException;

	void deleteById(Long id) throws ValidationException, MedicineNotFoundException;

}
