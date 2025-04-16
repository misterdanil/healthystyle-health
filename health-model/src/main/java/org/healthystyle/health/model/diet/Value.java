package org.healthystyle.health.model.diet;

public enum Value {
	CALORIE {
		@Override
		public String toString() {
			return "Калории";
		}

	},
	FAT {
		@Override
		public String toString() {
			return "Жиры";
		}

	},
	PROTEIN {
		@Override
		public String toString() {
			return "Белок";
		}

	},
	SUGAR {
		@Override
		public String toString() {
			return "Сахар";
		}

	},
	CARBOHYDRATE {
		@Override
		public String toString() {
			return "Углеводы";
		}

	}
}
