package org.healthystyle.health.web.measure.dto;

import org.healthystyle.health.model.measure.Type;

public interface MeasureTranslater {
	String translate(Type type);
}
