package org.healthystyle.health.service.dto;

public enum IndicatorTypeSort {
	NAME {
		@Override
		public String toString() {
			return "name";
		}
	},
	INDICATOR_CREATED_ON {
		@Override
		public String toString() {
			return "element(indicators).createdOn";
		}
	}
}
