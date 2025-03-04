package org.healthystyle.health.service.error;

public abstract class Field<T> {
	private T value;

	public Field(T value) {
		super();
		this.value = value;
	}

	public abstract String getName();

	public T getValue() {
		return value;
	}
}
