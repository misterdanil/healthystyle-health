package org.healthystyle.health.service.validation;

import java.time.Instant;

import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.model.measure.convert.FloatNumber;
import org.healthystyle.health.model.measure.convert.IntegerNumber;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.measure.convert.ConvertTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

public class ParamsChecker {
	private static final Logger LOG = LoggerFactory.getLogger(ParamsChecker.class);

	private ParamsChecker() {
	}

	public static void checkPageNumber(int pageNumber, BindingResult result) {
		LOG.debug("Checking page number");

		if (pageNumber < 1) {
			LOG.warn("Page number is zero or negative: {}", pageNumber);
			result.reject("*.*.pageNumber.zero_or_negative", "Номер страницы должен быть больше нуля");
		}
	}

	public static void checkLimit(int limit, final int MAX_SIZE, BindingResult result) {
		LOG.debug("Checking a limit '{}'", limit);

		if (limit < 1) {
			LOG.warn("The limit '{}' is less than 1. Returning 1", limit);
			result.reject("*.*.limit.zero_or_negative", "Лимит должен быть больше нуля");
		}
		if (limit > MAX_SIZE) {
			LOG.warn("The limit '{}' is more than max size '{}'. Returning max size", limit, MAX_SIZE);
			result.reject("*.*.limit.max_size",
					String.format("Лимит больше максимально допустимого пакета данных (%s)", MAX_SIZE));
		}
	}

	public static void checkDates(Instant fromDate, Instant toDate, BindingResult result) {
		LOG.debug("Checking from and to dates: {}, {}", fromDate, toDate);

		if (fromDate.isAfter(toDate)) {
			LOG.warn("From date '{}' is before to date '{}'", fromDate, toDate);
			result.reject("*.*.date.mixed", "Дата начала должны быть раньше даты конца");
		}
	}

	public static ConvertType checkConvertType(String weight, ConvertTypeService convertTypeService,
			BindingResult result) throws WeightNegativeOrZeroException, ConvertTypeNotRecognizedException {
		ConvertType convertType = convertTypeService.getConvertTypeByValue(weight);
		if ((convertType instanceof IntegerNumber && Integer.valueOf(weight) <= 0)
				|| (convertType instanceof FloatNumber && Float.valueOf(weight) <= 0)) {
			result.rejectValue("weight", "intake.update.weight.negative_or_zero",
					"Вес принимаемого лекарства должен быть больше нуля");
			throw new WeightNegativeOrZeroException(weight, result);
		}

		return convertType;
	}
}
