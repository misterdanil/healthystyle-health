package org.healthystyle.health.web.dto.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Service;

@Service
public class EnumToStringTranslator {
	
	@Named("enumToString")
	public String valueToString(Enum<?> enumValue) {
		if (enumValue == null)
			return null;
		return enumValue.toString();
	}
}
