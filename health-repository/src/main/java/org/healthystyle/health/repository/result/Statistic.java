package org.healthystyle.health.repository.result;

public abstract class Statistic {
	private Float value;

	public Statistic(Float value) {
		super();
		this.value = value;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
}
