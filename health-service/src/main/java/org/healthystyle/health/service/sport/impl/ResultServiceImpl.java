package org.healthystyle.health.service.sport.impl;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.healthystyle.health.model.sport.Result;
import org.healthystyle.health.model.sport.Set;
import org.healthystyle.health.repository.result.DateStatistic;
import org.healthystyle.health.repository.result.TimeStatistic;
import org.healthystyle.health.repository.sport.ResultRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.dto.sport.ResultSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.sport.ResultExistException;
import org.healthystyle.health.service.error.sport.ResultNotFoundException;
import org.healthystyle.health.service.error.sport.SetNotFoundException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.sport.ResultService;
import org.healthystyle.health.service.sport.SetService;
import org.healthystyle.health.service.validation.ParamsChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

@Service
public class ResultServiceImpl implements ResultService {
	@Autowired
	private ResultRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private SetService setService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(ResultServiceImpl.class);

	@Override
	public Result findById(Long id) throws ValidationException, ResultNotFoundException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "result");
		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("result.find.id.not_null", "Укажите идентификатор для поиска");
			throw new ValidationException("The id is null", result);
		}

		LOG.debug("Checking result for existence by id '{}'", id);
		Optional<Result> setResult = repository.findById(id);
		if (setResult.isEmpty()) {
			result.reject("result.find.not_found", "Не удалось найти результат");
			throw new ResultNotFoundException(id, result);
		}
		LOG.info("Got result successfully by id '{}'", id);

		return setResult.get();
	}

	@Override
	public Page<Result> findBySet(Long setId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_SET_PARAM_NAMES, setId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (setId == null) {
			result.reject("result.find.set_id.not_null", "Укажите идентификатор подхода упражнения для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<Result> results = repository.findBySet(setId, PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<Result> findByTrain(Long trainId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_TRAIN_PARAM_NAMES, trainId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (trainId == null) {
			result.reject("result.find.train_id.not_null", "Укажите идентификатор тренировки для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<Result> results = repository.findByTrain(trainId, PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<Result> findByDate(Instant date, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_DATE_PARAM_NAMES, date, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (date == null) {
			result.reject("result.find.date.not_null", "Укажите дату для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Long healthId = healthAccessor.getHealth().getId();

		Page<Result> results = repository.findByDate(date, healthId, PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<Result> findBySport(Long sportId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_SPORT_PARAM_NAMES, sportId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (sportId == null) {
			result.reject("result.find.sport_id.not_null", "Укажите занятие для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<Result> results = repository.findBySport(sportId, PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<Result> find(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PARAM_NAMES, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Long healthId = healthAccessor.getHealth().getId();

		Page<Result> results = repository.find(healthId, PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<DateStatistic> findPercentageBySport(Long sportId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PERCENTAGE_BY_SPORT_PARAM_NAMES, sportId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (sportId == null) {
			result.reject("result.find.sport_id.not_null", "Укажите занятие для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<DateStatistic> results = repository.findPercentageBySport(sportId, PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<DateStatistic> findPercentageByTrain(Long trainId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PERCENTAGE_BY_TRAIN_PARAM_NAMES, trainId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (trainId == null) {
			result.reject("result.find.train_id.not_null", "Укажите тренировку для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<DateStatistic> results = repository.findPercentageByTrain(trainId, PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<TimeStatistic> findPercentageByDate(LocalDate date, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PERCENTAGE_BY_DATE_PARAM_NAMES, date, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (date == null) {
			result.reject("result.find.date.not_null", "Укажите дату для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Long healthId = healthAccessor.getHealth().getId();

		Page<TimeStatistic> results = repository.findPercentageByDate(date, healthId, PageRequest.of(page, limit))
				.map(o -> new TimeStatistic(((Time) o[0]).toLocalTime(), Float.valueOf((String) o[1])));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<DateStatistic> findPercentageRangeDays(LocalDate start, LocalDate end, int page, int limit)
			throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PERCENTAGE_RANGE_WEEKS_PARAM_NAMES, start, end, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (start == null) {
			result.reject("result.find.start.not_null", "Укажите дату начала для поиска");
		}
		if (end == null) {
			result.reject("result.find.end.not_null", "Укажите дату конца для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Long healthId = healthAccessor.getHealth().getId();

		Page<DateStatistic> results = repository.findPercentageRangeDays(start, end, healthId,
				PageRequest.of(page, limit)).map(o -> new DateStatistic(((Timestamp)o[0]).toLocalDateTime().toLocalDate(), o[1] != null ? Float.valueOf((String)o[1]) : null));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<DateStatistic> findPercentageRangeWeeks(LocalDate start, LocalDate end, int page, int limit)
			throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PERCENTAGE_RANGE_WEEKS_PARAM_NAMES, start, end, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (start == null) {
			result.reject("result.find.start.not_null", "Укажите дату начала для поиска");
		}
		if (end == null) {
			result.reject("result.find.end.not_null", "Укажите дату конца для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Long healthId = healthAccessor.getHealth().getId();

		Page<DateStatistic> results = repository.findPercentageRangeWeeks(start, end, healthId,
				PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Page<DateStatistic> findPercentageRangeMonths(LocalDate start, LocalDate end, int page, int limit)
			throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PERCENTAGE_RANGE_MONTHS_PARAM_NAMES, start, end, page,
				limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "result");
		if (start == null) {
			result.reject("result.find.start.not_null", "Укажите дату начала для поиска");
		}
		if (end == null) {
			result.reject("result.find.end.not_null", "Укажите дату конца для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Long healthId = healthAccessor.getHealth().getId();

		Page<DateStatistic> results = repository.findPercentageRangeMonths(start, end, healthId,
				PageRequest.of(page, limit));
		LOG.info("Got results successfully by params: {}", params);

		return results;
	}

	@Override
	public Result save(ResultSaveRequest saveRequest, Long setId)
			throws ValidationException, ResultExistException, SetNotFoundException {
		LOG.debug("Validating result: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "result");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("Result is invalid: %s. Result: %s", result, saveRequest, result);
		}

		Set set = setService.findById(setId);

		Integer setNumber = saveRequest.getSetNumber();
		LocalDate date = saveRequest.getDate();
		LOG.debug("Checking result for existence by set '{}', set number '{}' and date '{}'", setId, setNumber, date);
		if (repository.existsBySetAndDateAndNumber(setId, date, setNumber)) {
			result.reject("result.save.exists", "Результат уже существует");
			throw new ResultExistException(setId, setNumber, date, result);
		}

		LOG.debug("The result is OK: {}", saveRequest);

		Result setResult = new Result(set, setNumber, saveRequest.getActualRepeat(), date);
		setResult = repository.save(setResult);
		LOG.info("Result was saved successfully: {}", result);

		return setResult;
	}

	@Override
	public void deleteById(Long id) throws ValidationException, ResultNotFoundException {
		Result result = findById(id);
		repository.delete(result);
		LOG.info("Result was deleted successfully by id '{}'", id);

	}

}
