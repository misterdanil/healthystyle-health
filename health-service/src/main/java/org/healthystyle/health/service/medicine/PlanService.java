package org.healthystyle.health.service.medicine;

import org.healthystyle.health.model.medicine.Plan;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.medicine.IntakeExistException;
import org.healthystyle.health.service.error.medicine.MedicineNotFoundException;
import org.healthystyle.health.service.error.medicine.PlanNotFoundException;
import org.healthystyle.health.service.error.medicine.PlanOverlapsException;
import org.healthystyle.health.service.error.medicine.TreatmentNotFoundException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.healthystyle.health.service.medicine.dto.PlanSaveRequest;
import org.healthystyle.health.service.medicine.dto.PlanUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlanService {
	static final String[] FIND_BY_MEDICINE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(PlanService.class,
			"findByMedicine", Long.class, int.class, int.class);

	static final String[] FIND_ACTUAL_PARAM_NAMES = MethodNameHelper.getMethodParamNames(PlanService.class,
			"findActual", int.class, int.class);

	static final String[] FIND_PARAM_NAMES = MethodNameHelper.getMethodParamNames(PlanService.class, "find", int.class,
			int.class);

	static final String[] FIND_BY_TREATMENT_PARAM_NAMES = MethodNameHelper.getMethodParamNames(PlanService.class,
			"findByTreatment", Long.class, int.class, int.class);

	Plan findById(Long planId) throws ValidationException, PlanNotFoundException;

	Page<Plan> findByMedicine(Long medicineId, int page, int limit) throws ValidationException;

	Page<Plan> findActual(int page, int limit) throws ValidationException;

	Page<Plan> find(int page, int limit) throws ValidationException;

	Page<Plan> findByTreatment(Long treatmentId, int page, int limit) throws ValidationException;

	Plan save(PlanSaveRequest saveRequest, Long treatmentId) throws ValidationException, TreatmentNotFoundException,
			MedicineNotFoundException, PlanOverlapsException, IntakeExistException, WeightNegativeOrZeroException,
			ConvertTypeNotRecognizedException, MeasureNotFoundException;

	void update(PlanUpdateRequest updateRequest, Long planId)
			throws ValidationException, PlanNotFoundException, PlanOverlapsException;

	void deleteById(Long id) throws ValidationException, PlanNotFoundException;
}
