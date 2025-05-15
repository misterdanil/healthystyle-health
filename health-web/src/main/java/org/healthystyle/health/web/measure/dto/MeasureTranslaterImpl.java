package org.healthystyle.health.web.measure.dto;

import org.healthystyle.health.model.measure.Type;
import org.springframework.stereotype.Component;

@Component
public class MeasureTranslaterImpl implements MeasureTranslater {

	@Override
	public String translate(Type type) {
		return type.toString();
	}

}
