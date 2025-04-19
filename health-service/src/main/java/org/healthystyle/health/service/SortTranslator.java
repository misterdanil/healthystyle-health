package org.healthystyle.health.service;

import org.springframework.data.domain.Sort;

public interface SortTranslator<T extends Enum<?>> {
	public Sort translateToSort(T sort);
}
