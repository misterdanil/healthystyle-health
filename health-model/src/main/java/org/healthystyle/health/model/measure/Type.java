package org.healthystyle.health.model.measure;

public enum Type {
	KG {
		@Override
		public String toString() {
			return "кг";
		}
	},
	GRAM {
		@Override
		public String toString() {
			return "г";
		}
	},
	CANTIMETRES {
		@Override
		public String toString() {
			return "см";
		}
	},
	MILLIMETRES {
		@Override
		public String toString() {
			return "мм";
		}
	},
	METRES {
		@Override
		public String toString() {
			return "м";
		}
	},

	// indicator
	MOLL_LITRES {
		@Override
		public String toString() {
			return "моль/литры";
		}
	},

	// medicine
	MILLIGRAM {
		@Override
		public String toString() {
			return "мг";
		}
	},
	MILLILITRES {
		@Override
		public String toString() {
			return "мл";
		}
	}
}
