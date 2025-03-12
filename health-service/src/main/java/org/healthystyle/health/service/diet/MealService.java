package org.healthystyle.health.service.diet;

import java.time.Instant;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.healthystyle.health.model.diet.Meal;
import org.healthystyle.health.service.diet.MealFoodService.MealFoodNotFoundException;
import org.healthystyle.health.service.dto.diet.MealReplaceRequest;
import org.healthystyle.health.service.dto.diet.MealSaveRequest;
import org.healthystyle.health.service.dto.diet.MealUpdateRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.diet.DietNotFoundException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodSetNotFoundException;
import org.healthystyle.health.service.error.diet.MealExistsException;
import org.healthystyle.health.service.error.diet.MealFoodExistException;
import org.healthystyle.health.service.error.diet.MealNotFoundException;
import org.healthystyle.health.service.error.diet.NoFoodsException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.springframework.data.domain.Page;

public interface MealService {
	Meal findById(Long id) throws MealNotFoundException;

	Page<Meal> findByFoods(List<Long> foodIds, int page, int limit) throws ValidationException;

	Page<Meal> findByDate(Instant date, int page, int limit) throws ValidationException;

	Page<Meal> findByDay(Integer day, int page, int limit) throws ValidationException;

	Page<Meal> findPlanned(int page, int limit) throws ValidationException;

	Page<Meal> findNextMeal(int page, int limit) throws ValidationException;

	Page<Meal> findByDiet(Long dietId, int page, int limit) throws ValidationException;

	Meal save(MealSaveRequest saveRequest, Long dietId) throws ValidationException, MealExistsException,
			DietNotFoundException, FoodNotFoundException, MealNotFoundException,
			MealFoodExistException, MeasureNotFoundException, ConvertTypeNotRecognizedException;

	Meal replace(MealReplaceRequest replaceRequest) throws ValidationException, MealExistsException,
			DietNotFoundException, FoodNotFoundException, FoodSetNotFoundException, MealNotFoundException,
			MealFoodExistException, MeasureNotFoundException, ConvertTypeNotRecognizedException;

	boolean existsByDietAndDayAndTime(Long dietId, Integer day, LocalTime time);

	boolean existsById(Long mealId) throws ValidationException;

	void deleteByIds(Set<Long> ids) throws ValidationException;

	void update(MealUpdateRequest updateRequest, Long mealId) throws ValidationException, MealNotFoundException,
			NoFoodsException, FoodNotFoundException, MealFoodExistException, MeasureNotFoundException,
			ConvertTypeNotRecognizedException, MealFoodNotFoundException, MealExistsException;

	public static void main(String[] args) {
		Test t = new Test();
		t.name = "John";
		t.car = "Audi";
		t.diet = "lol";
		Test t2 = new Test();
		t2.name = "Jim";
		t2.car = "BMW";
		t2.diet = "gena";

		Set<Test> s = new HashSet<>();
		s.add(t);
		s.add(t2);
		System.out.println(s);
		System.out.println(s.contains(new Test("John", "Audi", "lol")));

	}

	static class Test {
		private String name;
		private String diet;
		private String car;

		public Test() {
			super();
		}

		public Test(String name, String car, String diet) {
			super();
			this.name = name;
			this.diet = diet;
			this.car = car;
		}

		@Override
		public int hashCode() {
			return 5;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Test other = (Test) obj;
			return Objects.equals(car, other.car) && Objects.equals(name, other.name);
		}

		@Override
		public String toString() {
			return "Test [name=" + name + ", diet=" + diet + ", car=" + car + "]";
		}

	}
}
