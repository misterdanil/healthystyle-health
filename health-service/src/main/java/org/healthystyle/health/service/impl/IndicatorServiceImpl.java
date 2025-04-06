package org.healthystyle.health.service.impl;

import static java.util.Map.entry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.Indicator;
import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.model.measure.convert.FloatNumber;
import org.healthystyle.health.repository.IndicatorRepository;
import org.healthystyle.health.repository.result.AvgStatistic;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.IndicatorService;
import org.healthystyle.health.service.IndicatorTypeService;
import org.healthystyle.health.service.dto.IndicatorSaveRequest;
import org.healthystyle.health.service.dto.IndicatorSort;
import org.healthystyle.health.service.dto.IndicatorUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.indicator.IndicatorNotFoundException;
import org.healthystyle.health.service.error.indicator.IndicatorTypeNotFoundException;
import org.healthystyle.health.service.error.indicator.NotConvertableException;
import org.healthystyle.health.service.error.indicator.NotOwnerException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.validation.ParamsChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

@Service
public class IndicatorServiceImpl implements IndicatorService {
	@Autowired
	private IndicatorRepository repository;
	@Autowired
	private IndicatorTypeService indicatorTypeService;
	@Autowired
	private HealthAccessor healthAccessor;
	@Autowired
	private Validator validator;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(IndicatorServiceImpl.class);

	@Override
	public Indicator findById(Long id) throws IndicatorNotFoundException {
		LOG.debug("Getting an indicator by id '{}'", id);
		Optional<Indicator> indicator = repository.findById(id);
		if (indicator.isEmpty()) {
			BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicator");
			result.reject("indicator.find.id.notExist", "Индикатор по данному идентификатору не был найден");
			throw new IndicatorNotFoundException(id, result);
		}

		LOG.info("The indicator by id '{}' was found successfully", id);
		return indicator.get();
	}

	@Override
	public Page<Indicator> findByIndicatorType(Long indicatorTypeId, int page, int limit, IndicatorSort sort,
			Direction direction) throws ValidationException {
		String paramsTemplate = LogTemplate.getParamsTemplate(
				Map.ofEntries(entry("indicatorTypeId", indicatorTypeId), entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicator");
		if (indicatorTypeId == null) {
			result.rejectValue("indicatorTypeId", "indicator.find.indicator_type_id.notNull",
					"Укажите тип показателя здоровья");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching by indicator type. Params are invalid. Params: %s. Result: %s",
					result, paramsTemplate, result);
		}

		LOG.debug("Getting health for fetch by params '{}'", paramsTemplate);
		Long healthId = healthAccessor.getHealth().getId();

		Page<Indicator> indicators = repository.findByIndicatorType(indicatorTypeId, healthId,
				PageRequest.of(page, limit, Sort.by(direction, sort.toString())));
		LOG.info("Got indicators by params {} successfully", paramsTemplate);

		return indicators;
	}

	@Override
	public Page<Indicator> find(int page, int limit, IndicatorSort sort, Direction direction)
			throws ValidationException {
		String paramsTemplate = LogTemplate
				.getParamsTemplate(Map.ofEntries(entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicator");
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching by indicator type. Params are invalid. Params: %s. Result: %s",
					result, paramsTemplate, result);
		}

		LOG.debug("Getting health for fetch by params '{}'", paramsTemplate);
		Long healthId = healthAccessor.getHealth().getId();

		Page<Indicator> indicators = repository.find(healthId,
				PageRequest.of(page, limit, Sort.by(direction, sort.toString())));
		LOG.info("Got indicators by params {} successfully", paramsTemplate);

		return indicators;
	}

	@Override
	public Page<Indicator> findChangesByIndicatorType(Long indicatorTypeId, LocalDateTime from, LocalDateTime to,
			int page, int limit) throws ValidationException {
		String paramsTemplate = LogTemplate.getParamsTemplate(Map.ofEntries(entry("indicatorTypeId", indicatorTypeId),
				entry("from", from), entry("to", to), entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicator");
		if (indicatorTypeId == null) {
			result.rejectValue("indicatorTypeId", "indicator.find.indicator_type_id.notNull",
					"Укажите тип показателя здоровья");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		ParamsChecker.checkDates(from, to, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching by indicator type. Params are invalid. Params: %s. Result: %s",
					result, paramsTemplate, result);
		}

		LOG.debug("Getting health for fetch by params '{}'", paramsTemplate);
		Long healthId = healthAccessor.getHealth().getId();

		Page<Indicator> indicators = repository.findChangesByIndicatorType(indicatorTypeId, healthId, from, to,
				PageRequest.of(page, limit));
		LOG.info("Got indicators by params {} successfully", paramsTemplate);

		return indicators;
	}

	@Override
	public Page<AvgStatistic> findChangesByIndicatorTypeWeeksRange(Long indicatorTypeId, LocalDateTime from,
			LocalDateTime to, int page, int limit) throws ValidationException {
		String paramsTemplate = LogTemplate.getParamsTemplate(Map.ofEntries(entry("indicatorTypeId", indicatorTypeId),
				entry("from", from), entry("to", to), entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicator");
		if (indicatorTypeId == null) {
			result.rejectValue("indicatorTypeId", "indicator.find.indicator_type_id.notNull",
					"Укажите тип показателя здоровья");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		ParamsChecker.checkDates(from, to, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching by indicator type. Params are invalid. Params: %s. Result: %s",
					result, paramsTemplate, result);
		}

		LOG.debug("Getting health for fetch by params '{}'", paramsTemplate);
		Long healthId = healthAccessor.getHealth().getId();

		Page<AvgStatistic> indicators = repository.findChangesByIndicatorTypeWeeksRange(indicatorTypeId, healthId, from,
				to, PageRequest.of(page, limit));
		LOG.info("Got indicators by params {} successfully", paramsTemplate);

		return indicators;
	}

	@Override
	public Page<AvgStatistic> findChangesByIndicatorTypeYearsRange(Long indicatorTypeId, LocalDateTime from,
			LocalDateTime to, int page, int limit) throws ValidationException {
		String paramsTemplate = LogTemplate.getParamsTemplate(Map.ofEntries(entry("indicatorTypeId", indicatorTypeId),
				entry("from", from), entry("to", to), entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicator");
		if (indicatorTypeId == null) {
			result.rejectValue("indicatorTypeId", "indicator.find.indicator_type_id.notNull",
					"Укажите тип показателя здоровья");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		ParamsChecker.checkDates(from, to, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching by indicator type. Params are invalid. Params: %s. Result: %s",
					result, paramsTemplate, result);
		}

		LOG.debug("Getting health for fetch by params '{}'", paramsTemplate);
		Long healthId = healthAccessor.getHealth().getId();

		Page<AvgStatistic> indicators = repository.findChangesByIndicatorTypeYearsRange(indicatorTypeId, healthId, from,
				to, PageRequest.of(page, limit));
		LOG.info("Got indicators by params {} successfully", paramsTemplate);

		return indicators;
	}

	@Override
	public Page<AvgStatistic> findChangedByIndicatorTypeMonthsRange(Long indicatorTypeId, LocalDateTime from,
			LocalDateTime to, int page, int limit) throws ValidationException {
		String paramsTemplate = LogTemplate.getParamsTemplate(Map.ofEntries(entry("indicatorTypeId", indicatorTypeId),
				entry("from", from), entry("to", to), entry("page", page), entry("limit", limit)));

		LOG.debug("Validating params {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicator");
		if (indicatorTypeId == null) {
			result.rejectValue("indicatorTypeId", "indicator.find.indicator_type_id.notNull",
					"Укажите тип показателя здоровья");
		}

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		ParamsChecker.checkDates(from, to, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching by indicator type. Params are invalid. Params: %s. Result: %s",
					result, paramsTemplate, result);
		}

		LOG.debug("Getting health for fetch by params '{}'", paramsTemplate);
		Long healthId = healthAccessor.getHealth().getId();

		Page<AvgStatistic> indicators = repository.findChangesByIndicatorTypeMonthsRange(indicatorTypeId, healthId,
				from, to, PageRequest.of(page, limit));
		LOG.info("Got indicators by params {} successfully", paramsTemplate);

		return indicators;
	}

	@Override
	public Indicator save(IndicatorSaveRequest saveRequest)
			throws ValidationException, IndicatorTypeNotFoundException, NotConvertableException {
		LOG.debug("Validating params: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "indicator");
		validator.validate(saveRequest, result);

		LOG.debug("Checking indicator type '{}' for existence");
		Long indicatorTypeId = saveRequest.getIndicatorTypeId();
		IndicatorType indicatorType = indicatorTypeService.findById(indicatorTypeId);

		LOG.debug("Checking value for the convert type: {}", saveRequest);
		String value = saveRequest.getValue();
		ConvertType convertType = indicatorType.getConvertType();
		if (!convertType.support(value)) {
			result.rejectValue("value", "indicator.save.value.not_convertable",
					"Введённое значение не соответствует заданному формату для данного показателя здоровья");
			throw new NotConvertableException(value, convertType, result);
		}

		if (convertType instanceof FloatNumber) {
			value = getFormattedValue(value, convertType);
			saveRequest.setValue(value);
		}

		LOG.debug("Getting health for {}", saveRequest);
		Health health = healthAccessor.getHealth();

		Indicator indicator = new Indicator(saveRequest.getValue(), indicatorType, saveRequest.getDate(), health);
		indicator = repository.save(indicator);
		LOG.info("Indicator {} was saved successfully", saveRequest);

		return indicator;
	}

	@Override
	public void deleteById(Long id) throws NotOwnerException {
		LOG.debug("Getting health for deleting {}", id);
		Health health = healthAccessor.getHealth();
		if (!repository.existsByIdAndHealth(id, health.getId())) {
			BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicator");
			result.reject("indicator.delete.not_owner", "Вы не можете удалить чужой показатель");
			throw new NotOwnerException(id, health.getId(), result);
		}

		repository.deleteById(id);
		LOG.info("Indicator '{}' was deleted successfully", id);
	}

	@Override
	public void update(IndicatorUpdateRequest updateRequest)
			throws ValidationException, IndicatorNotFoundException, NotConvertableException {
		LOG.debug("Validating params: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "indicator");
		validator.validate(updateRequest, result);

		Long id = updateRequest.getId();
		LOG.debug("Getting indicator by id '{}'", id);
		Indicator indicator = findById(id);

		String value = updateRequest.getValue();
		if (!value.equals(indicator.getValue())) {
			LOG.debug("Value has been changed. Checking value for convertable");
			ConvertType convertType = indicator.getIndicatorType().getConvertType();
			if (!convertType.support(value)) {
				result.rejectValue("value", "indicator.update.value.not_convertable",
						"Введённое значение не соответствует заданному формату для данного показателя здоровья");
				throw new NotConvertableException(value, convertType, result);
			}
			if (convertType instanceof FloatNumber) {
				value = getFormattedValue(value, convertType);
				updateRequest.setValue(value);
			}
		}

		LocalDateTime date = updateRequest.getDate();
		if (!date.equals(indicator.getCreatedOn())) {
			LOG.debug("Date has been changed. Updating: {}", updateRequest);
			indicator.setCreatedOn(date);
		}

		LOG.debug("Updating data are valid: {}. Changing...", updateRequest);
		repository.save(indicator);
		LOG.info("Indicator was updated successfully from {} to {}", updateRequest, indicator);
	}

	private String getFormattedValue(String value, ConvertType convertType) {
		LOG.debug("{} belongs to float convert type", value);

		BigDecimal decimalValue = new BigDecimal(value);
		decimalValue.setScale(((FloatNumber) convertType).getRange(), RoundingMode.CEILING);

		String newValue = decimalValue.toString();
		LOG.debug("Formatted value from {} to {}", value, newValue);

		return newValue;
	}

}
