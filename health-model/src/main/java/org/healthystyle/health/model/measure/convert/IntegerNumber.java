package org.healthystyle.health.model.measure.convert;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "integer_number")
@DiscriminatorValue("int_numb")
public class IntegerNumber extends ConvertType {
	
}
