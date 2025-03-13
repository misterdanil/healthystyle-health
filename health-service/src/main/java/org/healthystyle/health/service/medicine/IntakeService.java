package org.healthystyle.health.service.medicine;

import java.time.Instant;

import org.healthystyle.health.model.medicine.Intake;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.medicine.IntakeNotFoundException;
import org.healthystyle.health.service.helper.MethodNameHelper;
import org.healthystyle.health.service.medicine.dto.IntakeSaveRequest;
import org.healthystyle.health.service.medicine.dto.IntakeUpdateRequest;
import org.springframework.data.domain.Page;

public interface IntakeService {
	static final String[] FIND_BY_MEDICINE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(IntakeService.class,
			"findByMedicine", String.class, int.class, int.class);
	static final String[] FIND_BY_PLAN_PARAM_NAMES = MethodNameHelper.getMethodParamNames(IntakeService.class,
			"findByPlan", Long.class, int.class, int.class);
	static final String[] FIND_BY_DAY_PARAM_NAMES = MethodNameHelper.getMethodParamNames(IntakeService.class,
			"findByDay", Integer.class, int.class, int.class);
	static final String[] FIND_BY_DATE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(IntakeService.class,
			"findByDate", Instant.class, int.class, int.class);
	static final String[] FIND_BY_CURRENT_DATE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(IntakeService.class,
			"findByCurrentDate", int.class, int.class);
	static final String[] FIND_PLANNED_PARAM_NAMES = MethodNameHelper.getMethodParamNames(IntakeService.class,
			"findPlanned", int.class, int.class);
	static final String[] FIND_NEXT_INTAKE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(IntakeService.class,
			"findNextIntake", int.class, int.class);
	
	Intake findById(Long id) throws ValidationException, IntakeNotFoundException;

	Page<Intake> findByMedicine(String name, int page, int limit) throws ValidationException;

	Page<Intake> findByPlan(Long planId, int page, int limit) throws ValidationException;

	Page<Intake> findByDay(Integer day, int page, int limit) throws ValidationException;

	Page<Intake> findByDate(Instant date, int page, int limit) throws ValidationException;

	Page<Intake> findByCurrentDate(int page, int limit) throws ValidationException;

	Page<Intake> findPlanned(int page, int limit) throws ValidationException;

	Page<Intake> findNextIntake(int page, int limit) throws ValidationException;

	Intake save(IntakeSaveRequest saveRequest, Long planId) throws ValidationException;

	void update(IntakeUpdateRequest updateRequest, Long intakeId) throws ValidationException, IntakeNotFoundException;

	void deleteById(Long id) throws ValidationException, IntakeNotFoundException;
}
