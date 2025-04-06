package org.healthystyle.health.model.measure.convert;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
@DiscriminatorValue("float_numb")
public class FloatNumber extends ConvertType {
	@Column(nullable = false, columnDefinition = "INT CONSTRAINT CK_min_range CHECK (range >= 1)")
	private Integer range;

	public FloatNumber() {
		super();
	}

	public FloatNumber(Integer range) {
		super();
		this.range = range;
	}

	public FloatNumber(BigDecimal minValue, BigDecimal maxValue, Integer range) {
		super(minValue, maxValue);
		this.range = range;
	}

	public Integer getRange() {
		return range;
	}

	public void setRange(Integer range) {
		this.range = range;
	}

	@Override
	public boolean support(String value) {
		try {
			Float.parseFloat(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
