import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.healthystyle.health.app.Main;
import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.diet.Diet;
import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.model.diet.FoodValue;
import org.healthystyle.health.model.diet.Meal;
import org.healthystyle.health.model.diet.MealFood;
import org.healthystyle.health.model.diet.NutritionValue;
import org.healthystyle.health.model.diet.Value;
import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.model.measure.convert.FloatNumber;
import org.healthystyle.health.model.medicine.Intake;
import org.healthystyle.health.model.medicine.Medicine;
import org.healthystyle.health.model.medicine.Plan;
import org.healthystyle.health.model.medicine.Sequence;
import org.healthystyle.health.model.medicine.Treatment;
import org.healthystyle.health.repository.diet.DietRepository;
import org.healthystyle.health.repository.diet.FoodRepository;
import org.healthystyle.health.repository.diet.FoodValueRepository;
import org.healthystyle.health.repository.diet.MealFoodRepository;
import org.healthystyle.health.repository.diet.MealRepository;
import org.healthystyle.health.repository.diet.NutritionValueRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.HealthService;
import org.healthystyle.health.service.diet.DietService;
import org.healthystyle.health.service.diet.FoodService;
import org.healthystyle.health.service.diet.NutritionValueService;
import org.healthystyle.health.service.dto.diet.DietSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodSaveRequest;
import org.healthystyle.health.service.dto.diet.FoodValueSaveRequest;
import org.healthystyle.health.service.dto.diet.MealFoodSaveRequest;
import org.healthystyle.health.service.dto.diet.MealSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeMismatchException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.diet.DietExistException;
import org.healthystyle.health.service.error.diet.FoodExistException;
import org.healthystyle.health.service.error.diet.FoodNotFoundException;
import org.healthystyle.health.service.error.diet.FoodValueExistException;
import org.healthystyle.health.service.error.diet.MealFoodNotFoundException;
import org.healthystyle.health.service.error.diet.MealSaveException;
import org.healthystyle.health.service.error.diet.MealTimeDuplicateException;
import org.healthystyle.health.service.error.diet.NutritionValueNotFoundException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.medicine.IntakeExistException;
import org.healthystyle.health.service.error.medicine.MedicineExistException;
import org.healthystyle.health.service.error.medicine.MedicineNotFoundException;
import org.healthystyle.health.service.error.medicine.PlanOverlapsException;
import org.healthystyle.health.service.error.medicine.TreatmentExistException;
import org.healthystyle.health.service.error.medicine.TreatmentNotFoundException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.medicine.dto.IntakeSaveRequest;
import org.healthystyle.health.service.medicine.dto.MedicineSaveRequest;
import org.healthystyle.health.service.medicine.dto.PlanSaveRequest;
import org.healthystyle.health.service.medicine.dto.TreatmentSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = Main.class)
public class DietTest {
	@Autowired
	private DietService service;
	@MockitoBean
	private HealthService healthService;
	@MockitoBean
	private DietRepository repository;
	@Autowired
	private FoodService foodService;
	@MockitoBean
	private FoodRepository foodRepository;
	@MockitoBean
	private HealthAccessor accessor;
	@MockitoBean
	private MealRepository mealRepository;
	@MockitoBean
	private MealFoodRepository mealFoodRepository;
	@MockitoBean
	private FoodValueRepository foodValueRepository;
	@MockitoBean
	private NutritionValueRepository nutritionValueRepository;
	@MockitoBean
	private DietRepository dietRepository;

	private Diet diet;
	private DietSaveRequest dietSaveRequest;

	private Food food;
	private FoodSaveRequest foodSaveRequest;

	private FoodValueSaveRequest foodValueSaveRequest1;
	private FoodValueSaveRequest foodValueSaveRequest2;
	private FoodValueSaveRequest foodValueSaveRequest3;

	private Measure measure = new Measure(Type.GRAM);
	private ConvertType convertType = new FloatNumber(2);

	private NutritionValue value1 = new NutritionValue(Value.CALORIE, measure, convertType);
	private NutritionValue value2 = new NutritionValue(Value.CARBOHYDRATE, measure, convertType);
	private NutritionValue value3 = new NutritionValue(Value.FAT, measure, convertType);

	private FoodValue foodValue1;
	private FoodValue foodValue2;
	private FoodValue foodValue3;

	private Meal meal;
	private MealSaveRequest mealSaveRequest;

	private MealFood mealFood;
	private MealFoodSaveRequest mealFoodSaveRequest;

	private Health health;

	@BeforeEach
	public void setup() {
		health = new Health(1L);
		health.setId(1L);

		diet = new Diet("Кето", LocalDate.now(), LocalDate.now().plusWeeks(1), health);
		diet.setId(1L);
		dietSaveRequest = new DietSaveRequest();
		dietSaveRequest.setTitle("Кето");
		dietSaveRequest.setStart(diet.getStart());
		dietSaveRequest.setEnd(diet.getEnd());

		food = new Food("Омлет", health);
		food.setId(1L);
		foodSaveRequest = new FoodSaveRequest();
		foodSaveRequest.setTitle(food.getTitle());

		List<FoodValueSaveRequest> foodValueSaveRequests = new ArrayList<>();

		foodValue1 = new FoodValue("20.6", value1, food);
		foodValue2 = new FoodValue("15.3", value2, food);
		foodValue3 = new FoodValue("50.3", value3, food);

		food.addFoodValue(foodValue1);
		food.addFoodValue(foodValue2);
		food.addFoodValue(foodValue3);

		foodValueSaveRequest1 = new FoodValueSaveRequest();
		foodValueSaveRequest1.setValue(foodValue1.getValue());
		foodValueSaveRequest1.setNutritionValue(value1.getValue());

		foodValueSaveRequest2 = new FoodValueSaveRequest();
		foodValueSaveRequest2.setValue(foodValue2.getValue());
		foodValueSaveRequest2.setNutritionValue(value2.getValue());

		foodValueSaveRequest3 = new FoodValueSaveRequest();
		foodValueSaveRequest3.setValue(foodValue3.getValue());
		foodValueSaveRequest3.setNutritionValue(value3.getValue());

		foodValueSaveRequests.add(foodValueSaveRequest1);
		foodValueSaveRequests.add(foodValueSaveRequest2);
		foodValueSaveRequests.add(foodValueSaveRequest3);

		foodSaveRequest.setFoodValues(foodValueSaveRequests);

		meal = new Meal(LocalTime.of(8, 30), 1, diet);
		meal.setId(1L);

		mealFood = new MealFood(meal, food, 350f, measure);
		mealFoodSaveRequest = new MealFoodSaveRequest();
		mealFoodSaveRequest.setFoodId(food.getId());
		mealFoodSaveRequest.setWeight(mealFood.getWeight());

		meal.addFood(mealFood);

		mealSaveRequest = new MealSaveRequest();
		mealSaveRequest.setDay(meal.getDay());
		mealSaveRequest.setTime(meal.getTime());

		List<MealFoodSaveRequest> mealFoodSaveRequests = new ArrayList<>();
		mealFoodSaveRequests.add(mealFoodSaveRequest);

		mealSaveRequest.setMealFoods(mealFoodSaveRequests);

		diet.addMeal(meal);
		List<MealSaveRequest> mealSaveRequests =new ArrayList<>();
		mealSaveRequests.add(mealSaveRequest);
		dietSaveRequest.setMeals(mealSaveRequests);
	}

	@Test
	public void createFoodTest() throws ValidationException, FoodExistException, NutritionValueNotFoundException,
			FoodValueExistException, FoodNotFoundException, ConvertTypeMismatchException {
		when(accessor.getHealth()).thenReturn(health);

		when(foodRepository.save(any(Food.class))).thenReturn(food);
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(nutritionValueRepository.findByValue(Value.CALORIE)).thenReturn(value1);
		when(nutritionValueRepository.findByValue(Value.CARBOHYDRATE)).thenReturn(value2);
		when(nutritionValueRepository.findByValue(Value.FAT)).thenReturn(value3);
		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		foodService.save(foodSaveRequest);
		verify(foodRepository, times(1)).save(any(Food.class));
	}

	@Test
	public void createFoodConvertTypeMismatchTest()
			throws ValidationException, FoodExistException, NutritionValueNotFoundException, FoodValueExistException,
			FoodNotFoundException, ConvertTypeMismatchException {
		food.setWeight("dadawd");
		when(accessor.getHealth()).thenReturn(health);

		when(foodRepository.save(any(Food.class))).thenReturn(food);
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(ConvertTypeMismatchException.class, () -> {
			
		});
	}

	@Test
	public void createFoodFoodExistTest()
			throws ValidationException, FoodExistException, NutritionValueNotFoundException, FoodValueExistException,
			FoodNotFoundException, ConvertTypeMismatchException {
		when(accessor.getHealth()).thenReturn(health);

		when(foodRepository.save(any(Food.class))).thenReturn(food);
		when(foodRepository.existsByTitle(food.getTitle(), health.getId())).thenReturn(true);
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(FoodExistException.class, () -> {
			foodService.save(foodSaveRequest);
		});
	}
	
	@Test
	public void createFoodBorderTest()
			throws ValidationException, FoodExistException, NutritionValueNotFoundException, FoodValueExistException,
			FoodNotFoundException, ConvertTypeMismatchException {
		when(accessor.getHealth()).thenReturn(health);

		when(foodRepository.save(any(Food.class))).thenReturn(food);
		when(foodRepository.existsByTitle(food.getTitle(), health.getId())).thenReturn(true);
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(FoodExistException.class, () -> {
			foodService.save(foodSaveRequest);
		});
	}

	@Test
	public void createFoodNutritionValueNotFoundTest()
			throws ValidationException, FoodExistException, NutritionValueNotFoundException, FoodValueExistException,
			FoodNotFoundException, ConvertTypeMismatchException {
		when(accessor.getHealth()).thenReturn(health);
		when(nutritionValueRepository.findByValue(Value.CARBOHYDRATE)).thenReturn(null);
		when(nutritionValueRepository.findByValue(Value.FAT)).thenReturn(null);

		when(foodRepository.save(any(Food.class))).thenReturn(food);
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(nutritionValueRepository.findByValue(Value.CALORIE)).thenReturn(null);
		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(NutritionValueNotFoundException.class, () -> {
			foodService.save(foodSaveRequest);
		});
	}
	
	@Test
	public void createDoetNutritionValueNotFoundTest()
			throws ValidationException, FoodExistException, NutritionValueNotFoundException, FoodValueExistException,
			FoodNotFoundException, ConvertTypeMismatchException {
		when(accessor.getHealth()).thenReturn(health);
		when(nutritionValueRepository.findByValue(Value.CARBOHYDRATE)).thenReturn(null);
		when(nutritionValueRepository.findByValue(Value.FAT)).thenReturn(null);

		when(foodRepository.save(any(Food.class))).thenReturn(food);
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(nutritionValueRepository.findByValue(Value.CALORIE)).thenReturn(null);
		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(NutritionValueNotFoundException.class, () -> {
			foodService.save(foodSaveRequest);
		});
	}

	@Test
	public void createFoodFoodValueExistTest()
			throws ValidationException, FoodExistException, NutritionValueNotFoundException, FoodValueExistException,
			FoodNotFoundException, ConvertTypeMismatchException {
		when(accessor.getHealth()).thenReturn(health);

		when(foodRepository.save(any(Food.class))).thenReturn(food);
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(nutritionValueRepository.findByValue(Value.CALORIE)).thenReturn(value1);
		when(nutritionValueRepository.findByValue(Value.CARBOHYDRATE)).thenReturn(value2);
		when(nutritionValueRepository.findByValue(Value.FAT)).thenReturn(value3);

		when(foodValueRepository.existsByFoodAndNutritionValue(any(Long.class), any(Value.class))).thenReturn(true);
		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(FoodValueExistException.class, () -> {
			foodService.save(foodSaveRequest);
		});
	}

	@Test
	public void createDietTest() throws ValidationException, DietExistException, MealTimeDuplicateException,
			MealFoodNotFoundException, MealSaveException {
		when(accessor.getHealth()).thenReturn(health);

		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(mealRepository.findById(any(Long.class))).thenReturn(Optional.of(meal));
		when(dietRepository.findById(any(Long.class))).thenReturn(Optional.of(diet));

		when(nutritionValueRepository.findByValue(Value.CALORIE)).thenReturn(value1);
		when(nutritionValueRepository.findByValue(Value.CARBOHYDRATE)).thenReturn(value2);
		when(nutritionValueRepository.findByValue(Value.FAT)).thenReturn(value3);
		
		when(dietRepository.save(any(Diet.class))).thenReturn(diet);
		when(mealRepository.save(any(Meal.class))).thenReturn(meal);

		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		service.save(dietSaveRequest);
		verify(dietRepository, times(1)).save(any(Diet.class));
	}
	
	@Test
	public void createDietExistTest() throws ValidationException, DietExistException, MealTimeDuplicateException,
			MealFoodNotFoundException, MealSaveException {
		when(accessor.getHealth()).thenReturn(health);

		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(mealRepository.findById(any(Long.class))).thenReturn(Optional.of(meal));
		when(dietRepository.findById(any(Long.class))).thenReturn(Optional.of(diet));

		when(nutritionValueRepository.findByValue(Value.CALORIE)).thenReturn(value1);
		when(nutritionValueRepository.findByValue(Value.CARBOHYDRATE)).thenReturn(value2);
		when(nutritionValueRepository.findByValue(Value.FAT)).thenReturn(value3);

		when(dietRepository.save(any(Diet.class))).thenReturn(diet);

		when(dietRepository.existsByTitle(diet.getTitle(), health.getId())).thenReturn(true);
		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(DietExistException.class, () -> {
			service.save(dietSaveRequest);
		});
	}
	
	@Test
	public void createDietMealTimeDuplicateTest() throws ValidationException, DietExistException, MealTimeDuplicateException,
			MealFoodNotFoundException, MealSaveException {
		when(accessor.getHealth()).thenReturn(health);
		
		MealSaveRequest mealSaveRequest2 = new MealSaveRequest();
		mealSaveRequest2.setDay(mealSaveRequest.getDay());
		mealSaveRequest2.setTime(mealSaveRequest.getTime());
		dietSaveRequest.getMeals().add(mealSaveRequest2);
		
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(mealRepository.findById(any(Long.class))).thenReturn(Optional.of(meal));
		when(dietRepository.findById(any(Long.class))).thenReturn(Optional.of(diet));

		when(nutritionValueRepository.findByValue(Value.CALORIE)).thenReturn(value1);
		when(nutritionValueRepository.findByValue(Value.CARBOHYDRATE)).thenReturn(value2);
		when(nutritionValueRepository.findByValue(Value.FAT)).thenReturn(value3);

		when(dietRepository.existsByTitle(diet.getTitle(), health.getId())).thenReturn(true);
		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(MealTimeDuplicateException.class, () -> {
			service.save(dietSaveRequest);
		});	}
	
	@Test
	public void createFoodWithoutNutritionValuesTest() throws ValidationException, DietExistException, MealTimeDuplicateException,
			MealFoodNotFoundException, MealSaveException {
		when(accessor.getHealth()).thenReturn(health);
		
		diet.setEnd(LocalDate.now().minusWeeks(3));
		
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(mealRepository.findById(any(Long.class))).thenReturn(Optional.of(meal));
		when(dietRepository.findById(any(Long.class))).thenReturn(Optional.of(diet));

		when(nutritionValueRepository.findByValue(Value.CALORIE)).thenReturn(value1);
		when(nutritionValueRepository.findByValue(Value.CARBOHYDRATE)).thenReturn(value2);
		when(nutritionValueRepository.findByValue(Value.FAT)).thenReturn(value3);

		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(ValidationException.class, () -> {
			service.save(dietSaveRequest);
		});	
		}
	
	@Test
	public void createDietExpiredTest() throws ValidationException, DietExistException, MealTimeDuplicateException,
			MealFoodNotFoundException, MealSaveException {
		when(accessor.getHealth()).thenReturn(health);
		
		diet.setEnd(LocalDate.now().minusWeeks(3));
		
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(mealRepository.findById(any(Long.class))).thenReturn(Optional.of(meal));
		when(dietRepository.findById(any(Long.class))).thenReturn(Optional.of(diet));

		when(nutritionValueRepository.findByValue(Value.CALORIE)).thenReturn(value1);
		when(nutritionValueRepository.findByValue(Value.CARBOHYDRATE)).thenReturn(value2);
		when(nutritionValueRepository.findByValue(Value.FAT)).thenReturn(value3);

		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(ValidationException.class, () -> {
			service.save(dietSaveRequest);
		});	
		}
	
	@Test
	public void createDietDatesMixedTest() throws ValidationException, DietExistException, MealTimeDuplicateException,
			MealFoodNotFoundException, MealSaveException {
		when(accessor.getHealth()).thenReturn(health);
		
		diet.setEnd(LocalDate.now().minusWeeks(3));
		
		when(foodRepository.findById(any(Long.class))).thenReturn(Optional.of(food));
		when(mealRepository.findById(any(Long.class))).thenReturn(Optional.of(meal));
		when(dietRepository.findById(any(Long.class))).thenReturn(Optional.of(diet));

		when(nutritionValueRepository.findByValue(Value.CALORIE)).thenReturn(value1);
		when(nutritionValueRepository.findByValue(Value.CARBOHYDRATE)).thenReturn(value2);
		when(nutritionValueRepository.findByValue(Value.FAT)).thenReturn(value3);

		when(foodValueRepository.save(foodValue1)).thenReturn(foodValue1);
		when(foodValueRepository.save(foodValue2)).thenReturn(foodValue2);
		when(foodValueRepository.save(foodValue3)).thenReturn(foodValue3);
		assertThrows(ValidationException.class, () -> {
			service.save(dietSaveRequest);
		});	
		}
	
	
//
//	@Test
//	public void createMedicineExistTest() throws ValidationException, MedicineExistException,
//			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
//		Health health = new Health(1L);
//		health.setId(1L);
//
//		when(accessor.getHealth()).thenReturn(health);
//
//		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
//		saveRequest.setName("Грипферон");
//		saveRequest.setMeasure(Type.MILLIGRAM);
//		saveRequest.setWeight("10");
//
//		when(medicineRepository.existsByName("Грипферон", 1L)).thenReturn(true);
//
//		assertThrows(MedicineExistException.class, () -> {
//			medicineService.save(saveRequest);
//		});
//	}
//
//	@Test
//	public void createMedicineWeightIncorrentTest() throws ValidationException, MedicineExistException,
//			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
//		Health health = new Health(1L);
//		health.setId(1L);
//
//		when(accessor.getHealth()).thenReturn(health);
//
//		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
//		saveRequest.setName("Грипферон");
//		saveRequest.setMeasure(Type.MILLIGRAM);
//		saveRequest.setWeight("-5");
//
//		Medicine medicine = new Medicine("Грипферон", health);
//
//		when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
//		medicineService.save(saveRequest);
//		verify(medicineRepository, times(1)).save(any(Medicine.class));
//
//		assertThrows(WeightNegativeOrZeroException.class, () -> {
//			medicineService.save(saveRequest);
//		});
//	}
//
//	@Test
//	public void createMedicineWeightNotRecognizedTest() throws ValidationException, MedicineExistException,
//			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
//		Health health = new Health(1L);
//		health.setId(1L);
//
//		when(accessor.getHealth()).thenReturn(health);
//
//		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
//		saveRequest.setName("Грипферон");
//		saveRequest.setMeasure(Type.MILLIGRAM);
//		saveRequest.setWeight("hello");
//
//		Medicine medicine = new Medicine("Грипферон", health);
//
//		when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
//		verify(medicineRepository, times(0)).save(any(Medicine.class));
//
//		assertThrows(ConvertTypeNotRecognizedException.class, () -> {
//			medicineService.save(saveRequest);
//		});
//	}
//
//	@Test
//	public void createMedicineMeasureNotPointedTest() throws ValidationException, MedicineExistException,
//			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
//		Health health = new Health(1L);
//		health.setId(1L);
//
//		when(accessor.getHealth()).thenReturn(health);
//
//		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
//		saveRequest.setName("Грипферон");
//		saveRequest.setMeasure(null);
//		saveRequest.setWeight("0");
//
//		Medicine medicine = new Medicine("Грипферон", health);
//
//		when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
//		verify(medicineRepository, times(0)).save(any(Medicine.class));
//
//		assertThrows(ValidationException.class, () -> {
//			medicineService.save(saveRequest);
//		});
//	}
//
//	@Test
//	public void createMedicineFieldIsNullTest() throws ValidationException, MedicineExistException,
//			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
//		Health health = new Health(1L);
//		health.setId(1L);
//
//		when(accessor.getHealth()).thenReturn(health);
//
//		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
//		saveRequest.setWeight("0");
//
//		Medicine medicine = new Medicine("Грипферон", health);
//
//		when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
//		verify(medicineRepository, times(0)).save(any(Medicine.class));
//
//		assertThrows(ValidationException.class, () -> {
//			medicineService.save(saveRequest);
//		});
//	}
//	
//	private Health health;
//	private Treatment treatment;
//	private TreatmentSaveRequest treatmentSaveRequest;
//	
//	private Medicine medicine;
//	private Plan plan;
//	private PlanSaveRequest planSaveRequest1;
//	
//	private Intake intake;
//	private IntakeSaveRequest intakeSaveRequest1;
//	
//	private Measure measureMilligram;
//	
//	@BeforeEach
//	public void setup() {
//		health = new Health(1L);
//		health.setId(1L);
//		
//		measureMilligram = new Measure(Type.MILLIGRAM);
//
//		treatment = new Treatment("ОРВИ", health);
//		treatment.setId(1L);
//
//		medicine = new Medicine("Грипферон", health);
//		plan = new Plan(medicine, LocalDate.now(), LocalDate.now().plusWeeks(1), treatment, health);
//		plan.setId(1L);
//
//		intake = new Intake(plan, LocalTime.of(8, 30), 1);
//		intake.setMeasure(measureMilligram);
//		intake.setId(1L);
//		intake.setWeight("500");
//
//		treatmentSaveRequest = new TreatmentSaveRequest();
//		treatmentSaveRequest.setDescription("ОРВИ");
//
//		planSaveRequest1 = new PlanSaveRequest();
//		planSaveRequest1.setMedicineId(1L);
//		planSaveRequest1.setStart(LocalDate.now());
//		planSaveRequest1.setEnd(LocalDate.now().plusWeeks(1));
//		planSaveRequest1.setSequence(Sequence.AFTER_EAT);
//
//		List<PlanSaveRequest> plans = new ArrayList<PlanSaveRequest>();
//		plans.add(planSaveRequest1);
//
//		treatmentSaveRequest.setPlans(plans);
//
//		intakeSaveRequest1 = new IntakeSaveRequest();
//		intakeSaveRequest1.setDay(0);
//		intakeSaveRequest1.setTime(LocalTime.of(8, 30));
//		intakeSaveRequest1.setMeasureType(Type.MILLIGRAM);
//		intakeSaveRequest1.setWeight("500");
//		List<IntakeSaveRequest> intakes = new ArrayList<>();
//		intakes.add(intakeSaveRequest1);
//		
//		planSaveRequest1.setIntakes(intakes);
//	}
//
//	@Test
//	public void createTreatmentTest() throws ValidationException, MedicineNotFoundException, TreatmentExistException,
//			TreatmentNotFoundException, PlanOverlapsException, IntakeExistException, WeightNegativeOrZeroException,
//			ConvertTypeNotRecognizedException, MeasureNotFoundException {
//		when(accessor.getHealth()).thenReturn(health);
//
//		when(repository.save(any(Treatment.class))).thenReturn(treatment);
//		when(planRepository.save(any(Plan.class))).thenReturn(plan);
//		when(intakeRepository.save(any(Intake.class))).thenReturn(intake);
//
//		when(medicineRepository.findById(any(Long.class))).thenReturn(Optional.of(medicine));
//		when(repository.findById(1L)).thenReturn(Optional.of(treatment));
//		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
//		service.save(treatmentSaveRequest);
//		verify(repository, times(1)).save(any(Treatment.class));
//	}
//
//	@Test
//	public void createTreatmentMedicineNotFoundTest() throws ValidationException, MedicineNotFoundException,
//			TreatmentExistException, TreatmentNotFoundException, PlanOverlapsException, IntakeExistException,
//			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
//		when(accessor.getHealth()).thenReturn(health);
//
//		when(repository.save(any(Treatment.class))).thenReturn(treatment);
//		when(planRepository.save(any(Plan.class))).thenReturn(plan);
//		when(intakeRepository.save(any(Intake.class))).thenReturn(intake);
//
//		when(medicineRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//		when(repository.findById(1L)).thenReturn(Optional.of(treatment));
//		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
//		assertThrows(MedicineNotFoundException.class, () -> {
//			service.save(treatmentSaveRequest);
//		});
//	}
//	
//	@Test
//	public void createTreatmentExistTest() throws ValidationException, MedicineNotFoundException,
//			TreatmentExistException, TreatmentNotFoundException, PlanOverlapsException, IntakeExistException,
//			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
//		when(accessor.getHealth()).thenReturn(health);
//
//		when(repository.save(any(Treatment.class))).thenReturn(treatment);
//		when(planRepository.save(any(Plan.class))).thenReturn(plan);
//		when(intakeRepository.save(any(Intake.class))).thenReturn(intake);
//		
//		when(repository.existsByDescription("ОРВИ", 1L)).thenReturn(true);
//		
//		when(medicineRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//		when(repository.findById(1L)).thenReturn(Optional.of(treatment));
//		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
//		assertThrows(MedicineNotFoundException.class, () -> {
//			service.save(treatmentSaveRequest);
//		});
//	}
//	
//	@Test
//	public void createPlanOverlapsTest() throws ValidationException, MedicineNotFoundException,
//			TreatmentExistException, TreatmentNotFoundException, PlanOverlapsException, IntakeExistException,
//			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
//		when(accessor.getHealth()).thenReturn(health);
//
//		when(repository.save(any(Treatment.class))).thenReturn(treatment);
//		when(planRepository.save(any(Plan.class))).thenReturn(plan);
//		when(intakeRepository.save(any(Intake.class))).thenReturn(intake);
//		
//		when(planRepository.existsOverlaps(LocalDate.now().minusWeeks(1), LocalDate.now().plusWeeks(2), 1L, null)).thenReturn(true);
//		
//		when(medicineRepository.findById(any(Long.class))).thenReturn(Optional.of(medicine));
//		when(repository.findById(1L)).thenReturn(Optional.of(treatment));
//		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
//		assertThrows(PlanOverlapsException.class, () -> {
//			service.save(treatmentSaveRequest);
//		});
//	}
//	
//	@Test
//	public void createIntakeExistTest() throws ValidationException, MedicineNotFoundException,
//			TreatmentExistException, TreatmentNotFoundException, PlanOverlapsException, IntakeExistException,
//			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
//		when(accessor.getHealth()).thenReturn(health);
//
//		when(repository.save(any(Treatment.class))).thenReturn(treatment);
//		when(planRepository.save(any(Plan.class))).thenReturn(plan);
//		when(intakeRepository.save(any(Intake.class))).thenReturn(intake);
//		
//		when(intakeRepository.existsByTimeAndDayAndPlanId(LocalTime.of(8, 30), 1, 1L)).thenReturn(true);
//		
//		when(medicineRepository.findById(any(Long.class))).thenReturn(Optional.of(medicine));
//		when(repository.findById(1L)).thenReturn(Optional.of(treatment));
//		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
//		assertThrows(IntakeExistException.class, () -> {
//			service.save(treatmentSaveRequest);
//		});
//	}
}
