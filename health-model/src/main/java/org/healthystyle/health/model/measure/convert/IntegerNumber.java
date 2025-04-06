package org.healthystyle.health.model.measure.convert;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "integer_number")
@DiscriminatorValue("int_numb")
public class IntegerNumber extends ConvertType {

	public IntegerNumber() {
		super();
	}

	@Override
	public boolean support(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
