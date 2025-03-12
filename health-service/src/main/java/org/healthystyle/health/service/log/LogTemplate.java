package org.healthystyle.health.service.log;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	public static String getParamsTemplate(String[] paramNames, Object... values) {
		if (paramNames.length != values.length) {
			throw new RuntimeException(
					"The param names length is " + paramNames.length + ", but values length is " + values.length);
		}
		return String
				.join(", ", IntStream.range(0, paramNames.length).mapToObj(i -> paramNames[i] + " '" + values[i] + "'")
						.collect(Collectors.toList()).toArray(String[]::new))
				.trim();
	}

	public static void main(String[] args) {
		Map<String, Object> mapa = new LinkedHashMap<>();
		mapa.put("name", "John");
		mapa.put("page", 1);
		mapa.put("limit", 5);
		System.out.println(getParamsTemplate(mapa));
	}
}
