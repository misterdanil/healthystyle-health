package org.healthystyle.health.service.impl;

import static java.util.Map.entry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.repository.IndicatorTypeRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.IndicatorTypeService;
import org.healthystyle.health.service.dto.IndicatorTypeSaveRequest;
import org.healthystyle.health.service.dto.IndicatorTypeSort;
import org.healthystyle.health.service.dto.IndicatorTypeUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.indicator.IndicatorTypeNotFoundException;
import org.healthystyle.health.service.error.indicator.NameExistedException;
import org.healthystyle.health.service.error.indicator.NotCreatorException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.measure.convert.ConvertTypeNotFoundException;
import org.healthystyle.health.service.log.LogTemplate;
import org.healthystyle.health.service.measure.MeasureService;
import org.healthystyle.health.service.measure.convert.ConvertTypeService;
import org.healthystyle.health.service.validation.ParamsChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

@Service
public class IndicatorTypeServiceImpl implements IndicatorTypeService {
	@Autowired
	private IndicatorTypeRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private MeasureService measureService;
	@Autowired
	private ConvertTypeService convertTypeService;
	@Autowired
	private HealthAccessor healthAccessor;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(IndicatorTypeServiceImpl.class);

	@Override
	@Cacheable
	public IndicatorType findById(Long id) throws IndicatorTypeNotFoundException {
		LOG.debug("Fetching an indicator type by id '{}'", id);
		Optional<IndicatorType> indicatorType = repository.findById(id);

		if (indicatorType.isEmpty()) {
			BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicatorType");
			result.reject("indicator_type.find.id.notExist");
			throw new IndicatorTypeNotFoundException(id, result);
		}

		LOG.info("The indicator type by id '{}' was found successfully", id);

		return indicatorType.get();
	}

	@Override
	public Page<IndicatorType> findByName(String name, int page, int limit) throws ValidationException {
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("name", name);
		params.put("page", page);
		params.put("limit", limit);

		String paramsTemplate = LogTemplate.getParamsTemplate(params);

		LOG.debug("Getting indicator types by {}", paramsTemplate);

		LOG.debug("Validating params: {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicatorType");

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, limit, result);

		LOG.debug("Checking a name of indicator type '{}'", name);
		if (name == null) {
			result.reject("indicator_type.find.name.notNull", "Введите название для поиска");
		}

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching indicator types. Passed data are invalid. Params: %s. Result: %s",
					result, paramsTemplate, result);
		}

		LOG.debug("Passed data are valid. Params: {}", paramsTemplate);

		Page<IndicatorType> indicatorTypes = repository.findByName(name, PageRequest.of(page, limit));
		LOG.info("Got indicator types successfully by params {}", paramsTemplate);

		return indicatorTypes;
	}

	@Override
	public Page<IndicatorType> find(int page, int limit, IndicatorTypeSort sort) throws ValidationException {
		Map<String, Object> params = Map.ofEntries(entry("page", page), entry("limit", limit));
		String paramsTemplate = LogTemplate.getParamsTemplate(params);

		LOG.debug("Getting indicator types by {}", paramsTemplate);

		LOG.debug("Validating params: {}", paramsTemplate);
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicatorType");

		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while fetching indicator types. Passed data are invalid. Params: %s. Result: %s",
					result, paramsTemplate, result);
		}

		Page<IndicatorType> indicatorTypes;
		if (sort.equals(IndicatorTypeSort.INDICATOR_CREATED_ON)) {
			indicatorTypes = repository.findSortByIndicatorCreatedOn(PageRequest.of(page, limit));
		} else {
			indicatorTypes = repository.find(PageRequest.of(page, limit, Sort.by(Direction.ASC, sort.toString())));
		}
		LOG.info("Got indicator types successfully by {}", paramsTemplate);

		return indicatorTypes;
	}

	@Override
	public IndicatorType save(IndicatorTypeSaveRequest saveRequest)
			throws ValidationException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException {
		LOG.debug("Start saving indicator type: {}", saveRequest);

		LOG.debug("Validating indicator type: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "indicatorType");
		validator.validate(saveRequest, result);

		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while saving an indicator type. Passed data are invalid. Result: %s", result,
					result);
		}
		LOG.debug("Passed data are valid: {}", saveRequest);

		String name = saveRequest.getName();
		LOG.debug("Checking existence for a name '{}'", name);
		if (repository.existsByName(name)) {
			result.rejectValue("name", "indicator_type.save.name.exists",
					"Тип индикатора с таким названием уже существует");
			throw new NameExistedException(name, result);
		}

		Type type = saveRequest.getType();
		LOG.debug("Getting measure by type '{}", type);
		Measure measure = measureService.findByType(type);

		Long convertTypeId = saveRequest.getConvertTypeId();
		LOG.debug("Getting convert type by id '{}'", convertTypeId);
		ConvertType convertType = convertTypeService.findById(convertTypeId);

		LOG.debug("Getting id of the creator");
		Long creator = Long.valueOf(healthAccessor.getHealth().getUserId());

		LOG.debug("Forming indicator type by params {} and creator {}", saveRequest, creator);
		IndicatorType indicatorType = new IndicatorType(saveRequest.getName(), measure, convertType, creator);

		indicatorType = repository.save(indicatorType);
		LOG.info("An indicator type '{}' was saved successfully", indicatorType);

		return indicatorType;
	}

	@Override
	public void deleteById(Long id) throws NotCreatorException {
		LOG.debug("Start deleting an indicator type by id '{}'", id);

		LOG.debug("Getting a creator");
		Long creator = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "indicatorType");
		LOG.debug("Checking for belonging indicator type '{}' to current user '{}'", id, creator);
		if (!repository.existsByIdAndCreator(id, creator)) {
			result.reject("indicator_type.delete.notCreator",
					"Вы не можете удалить тип индикатора, созданный другим пользователем");
			throw new NotCreatorException(id, creator, result);
		}

		repository.deleteById(id);
	}

	@Override
	public void update(IndicatorTypeUpdateRequest updateRequest)
			throws ValidationException, IndicatorTypeNotFoundException, NotCreatorException, NameExistedException,
			MeasureNotFoundException, ConvertTypeNotFoundException {
		LOG.debug("Checking indicator type for update: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "indicatorType");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException(
					"Exception occurred while updating indicator type. Indicator type is invalid. Result: %s", result,
					result);
		}

		Long id = updateRequest.getId();
		IndicatorType indicatorType = findById(id);

		LOG.debug("Getting creator");
		Long creator = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

		LOG.debug("Checking for belonging of indicator type '{}' to creator '{}'", id, creator);
		if (!repository.existsByIdAndCreator(id, creator)) {
			result.rejectValue("id", "indicator_type.update.not_creator",
					"Вы не можете изменять не вами созданные типы индикаторов");
			throw new NotCreatorException(id, creator, result);
		}

		String name = updateRequest.getName();
		if (!name.equals(indicatorType.getName())) {
			LOG.debug("The name was changed '{}'. Checking existence for this", name);
			if (repository.existsByName(name)) {
				result.rejectValue("name", "indicator_type.update.name.exists",
						"Тип индикатора с таким именем уже существует");
				throw new NameExistedException(name, result);
			}
			LOG.debug("Changing the name '{}' of '{}'", name, id);
			indicatorType.setName(name);
		}

		Type type = updateRequest.getType();
		if (!type.equals(indicatorType.getMeasure().getType())) {
			Measure measure = measureService.findByType(type);
			LOG.debug("Changing the measure type '{}' of the '{}'", type, id);
			indicatorType.setMeasure(measure);
		}

		Long convertTypeId = updateRequest.getConvertTypeId();
		if (!convertTypeId.equals(indicatorType.getConvertType().getId())) {
			ConvertType convertType = convertTypeService.findById(convertTypeId);
			LOG.debug("Changing the convert type '{}' of the '{}'", convertTypeId, id);
			indicatorType.setConvertType(convertType);
		}

		LOG.debug("The passed data are valid: {}. Updating data");
		repository.save(indicatorType);
		LOG.info("The indicator type '{}' was updated successfully", id);
	}

}
