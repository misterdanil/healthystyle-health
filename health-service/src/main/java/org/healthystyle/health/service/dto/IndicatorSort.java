package org.healthystyle.health.service.dto;

public enum IndicatorSort {
	VALUE {

		@Override
		public String toString() {
			return "value";
		}

	},
	DATE {

		@Override
		public String toString() {
			return "createdOn";
		}

	},
	TYPE {

		@Override
		public String toString() {
			return "indicatorType.name";
		}

	}
}
