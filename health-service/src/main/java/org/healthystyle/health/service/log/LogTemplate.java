package org.healthystyle.health.service.log;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LogTemplate {
	private LogTemplate() {
	}

	public static String getParamsTemplate(String specifier, String... paramNames) {
		return String.join(", ", Arrays.asList(paramNames).stream().map(param -> param + " '" + specifier + "'")
				.collect(Collectors.toList()).toArray(new String[paramNames.length])).trim();
	}
	
	public static String getParamsTemplate(Map<String, Object> params) {
		return String.join(", ", params.keySet().stream().map(param -> param + " '" + params.get(param) + "'")
				.collect(Collectors.toList()).toArray(new String[params.size()])).trim();
	}
	
	public static void main(String[] args) {
		Map<String, Object> mapa = new LinkedHashMap<>();
		mapa.put("name", "John");
		mapa.put("page", 1);
		mapa.put("limit", 5);
		System.out.println(getParamsTemplate(mapa));
	}
}
