package org.healthystyle.health.service.measure.convert.impl;

import java.util.LinkedHashMap;
import java.util.Optional;

import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.repository.measure.convert.ConvertTypeRepository;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.convert.ConvertTypeNotFoundException;
import org.healthystyle.health.service.measure.convert.ConvertTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@Service
public class ConvertTypeServiceImpl implements ConvertTypeService {
	@Autowired
	private ConvertTypeRepository repository;

	private static final Logger LOG = LoggerFactory.getLogger(ConvertTypeServiceImpl.class);

	@Override
	public ConvertType findById(Long id) throws ValidationException, ConvertTypeNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "convertType");

		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("convert_type.find.id.not_null", "Укажите идентификатор типа данных для поиска");
			throw new ValidationException("The id is null", result);
		}

		LOG.debug("Checking convert type for existence by id '{}'", id);
		Optional<ConvertType> type = repository.findById(id);
		if (type.isEmpty()) {
			result.reject("convert_type.find.not_found", "Не удалось найти тип данных");
			throw new ConvertTypeNotFoundException(id, result);
		}
		LOG.info("Got convert type successfully by id '{}'", id);

		return type.get();
	}

	@Override
	public ConvertType getConvertTypeByValue(String value) throws ConvertTypeNotRecognizedException {
		value = value.trim();

		LOG.debug("Checking value for integer: {}", value);
		try {
			Integer.valueOf(value);
		} catch (NumberFormatException e) {
			LOG.debug("Not integer: {}", value);
		}

		LOG.debug("Checking value for float: {}", value);
		try {
			Float.valueOf(value);
			int ranges = value.substring(value.indexOf(".") + 1).length();
			LOG.debug("Getting range: {}", ranges);
			ConvertType type = repository.findByRange(ranges);
			if (type != null) {
				return type;
			}
		} catch (NumberFormatException e) {
			LOG.debug("Not float: {}", value);
		}
		
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "convertType");
		result.reject("convert_type.find.value.not_recognized", "Неизвестный тип данных");
		throw new ConvertTypeNotRecognizedException(result, value);
	}

	public static void main(String[] args) {
		String value = "55.";
		Float.valueOf(value);
		String ranges = value.substring(value.indexOf(".") + 1);
		System.out.println(ranges.length());
	}

}
