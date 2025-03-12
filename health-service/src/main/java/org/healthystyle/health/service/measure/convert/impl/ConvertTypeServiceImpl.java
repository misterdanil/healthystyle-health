package org.healthystyle.health.service.measure.convert.impl;

import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.convert.ConvertTypeNotFoundException;
import org.healthystyle.health.service.measure.convert.ConvertTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConvertTypeServiceImpl implements ConvertTypeService {
	@Autowired
	private ConvertTypeRepository repository;

	@Override
	public ConvertType findById(Long id) throws ConvertTypeNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConvertType getConvertTypeByValue(String value) throws ConvertTypeNotRecognizedException {
		
	}

}
