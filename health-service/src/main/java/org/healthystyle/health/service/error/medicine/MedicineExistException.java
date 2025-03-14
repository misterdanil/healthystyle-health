package org.healthystyle.health.service.error.medicine;

import org.healthystyle.health.service.error.AbstractException;
import org.springframework.validation.BindingResult;

public class MedicineExistException extends AbstractException {
	private String name;

	public MedicineExistException(String name, BindingResult result) {
		super(result, "A medicine with name '%s' has already existed", name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
