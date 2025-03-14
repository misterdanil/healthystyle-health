package org.healthystyle.health.service.medicine;

import org.healthystyle.health.model.medicine.Treatment;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.medicine.TreatmentExistException;
import org.healthystyle.health.service.error.medicine.TreatmentNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.healthystyle.health.service.medicine.dto.TreatmentSaveRequest;
import org.healthystyle.health.service.medicine.dto.TreatmentUpdateRequest;
import org.springframework.data.domain.Page;

public interface TreatmentService {
	static final String[] FIND_BY_DESCRIPTION_PARAM_NAMES = MethodNameHelper.getMethodParamNames(TreatmentService.class,
			"findByDescription", String.class, int.class, int.class);

	static final String[] FIND_PARAM_NAMES = MethodNameHelper.getMethodParamNames(TreatmentService.class, "find",
			int.class, int.class);

	Treatment findById(Long id) throws TreatmentNotFoundException, ValidationException;

	Page<Treatment> findByDescription(String description, int page, int limit) throws ValidationException;

	Page<Treatment> find(int page, int limit) throws ValidationException;

	Treatment save(TreatmentSaveRequest saveRequest) throws ValidationException, TreatmentExistException;

	void update(TreatmentUpdateRequest updateRequest, Long id)
			throws ValidationException, TreatmentNotFoundException, TreatmentExistException;

	void deleteById(Long id) throws ValidationException, TreatmentNotFoundException;
}
