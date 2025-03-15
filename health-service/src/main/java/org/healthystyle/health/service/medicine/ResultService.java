package org.healthystyle.health.service.medicine;

import org.healthystyle.health.model.medicine.Result;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.medicine.IntakeNotFoundException;
import org.healthystyle.health.service.error.medicine.ResultExistException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.healthystyle.health.service.medicine.dto.ResultSaveRequest;
import org.springframework.data.domain.Page;

public interface ResultService {
	static final String[] FIND_BY_INTAKE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ResultService.class,
			"findByIntake", Long.class, int.class, int.class);

	static final String[] FIND_BY_PLAN_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ResultService.class,
			"findByPlan", Long.class, int.class, int.class);

	static final String[] FIND_PARAM_NAMES = MethodNameHelper.getMethodParamNames(ResultService.class, "find",
			int.class, int.class);

	Page<Result> findByIntake(Long intakeId, int page, int limit) throws ValidationException;

	Page<Result> findByPlan(Long planId, int page, int limit) throws ValidationException;

	Page<Result> find(int page, int limit) throws ValidationException;

	Result save(ResultSaveRequest saveRequest, Long intakeId)
			throws ValidationException, ResultExistException, IntakeNotFoundException;

	void deleteById(Long id) throws ValidationException;

}
