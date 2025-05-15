package org.healthystyle.health.model.measure;

public enum Type {
	KG {
		@Override
		public String toString() {
			return "Килограммы";
		}
	},
	GRAM {
		@Override
		public String toString() {
			return "Граммы";
		}
	},
	CANTIMETRES {
		@Override
		public String toString() {
			return "Сантиметры";
		}
	},
	MILLIMETRES {
		@Override
		public String toString() {
			return "Миллиметры";
		}
	},
	METRES {
		@Override
		public String toString() {
			return "Метры";
		}
	},

	// indicator
	MOLL_LITRES {
		@Override
		public String toString() {
			return "Моль/литры";
		}
	},

	// medicine
	MILLIGRAM {
		@Override
		public String toString() {
			return "Миллиграммы";
		}
	},
	MILLILITRES {
		@Override
		public String toString() {
			return "Миллилитры";
		}
	}
}
