package org.healthystyle.health.service.measure.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.repository.measure.MeasureRepository;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.measure.MeasureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@Service
public class MeasureServiceImpl implements MeasureService {
	@Autowired
	private MeasureRepository repository;

	private static final Logger LOG = LoggerFactory.getLogger(MeasureServiceImpl.class);

	@Override
	public Measure findByType(Type type) throws ValidationException, MeasureNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "measure");

		LOG.debug("Checking type for not null");
		if (type == null) {
			result.reject("measure.find.type.not_null", "Укажите тип измерения для поиска");
			throw new ValidationException("The id is null", result);
		}

		Measure measure = repository.findByType(type);
		LOG.debug("Checking measure for existence by type '{}'", type);
		if (measure == null) {
			result.reject("measure.find.not_found", "Не удалось найти тип измерения");
			throw new MeasureNotFoundException(type, result);
		}
		LOG.info("Got measure by type '{}' successfully", type);

		return measure;
	}

	@Override
	public List<Measure> findAll() {
		return repository.findAll();
	}

}
