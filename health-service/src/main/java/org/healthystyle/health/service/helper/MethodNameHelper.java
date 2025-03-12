package org.healthystyle.health.service.helper;

import java.util.Arrays;

import org.healthystyle.health.service.diet.impl.MealFoodServiceImpl;

public class MethodNameHelper {
	private MethodNameHelper() {
	}

	public static String[] getMethodParamNames(Class<?> clazz, String methodName, Class<?>... params) {
		try {
			return Arrays.stream(
					clazz.getMethod(methodName, params).getParameters())
					.map(p -> p.getName()).toArray(String[]::new);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Exception occurred while getting method param names", e);
		}
	}
}
