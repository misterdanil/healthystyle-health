
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

import org.assertj.core.util.Arrays;
import org.healthystyle.health.app.Main;
import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.medicine.Intake;
import org.healthystyle.health.model.medicine.Medicine;
import org.healthystyle.health.model.medicine.Plan;
import org.healthystyle.health.model.medicine.Sequence;
import org.healthystyle.health.model.medicine.Treatment;
import org.healthystyle.health.repository.medicine.IntakeRepository;
import org.healthystyle.health.repository.medicine.MedicineRepository;
import org.healthystyle.health.repository.medicine.PlanRepository;
import org.healthystyle.health.repository.medicine.TreatmentRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.HealthService;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeNotRecognizedException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.medicine.IntakeExistException;
import org.healthystyle.health.service.error.medicine.MedicineExistException;
import org.healthystyle.health.service.error.medicine.MedicineNotFoundException;
import org.healthystyle.health.service.error.medicine.PlanOverlapsException;
import org.healthystyle.health.service.error.medicine.TreatmentExistException;
import org.healthystyle.health.service.error.medicine.TreatmentNotFoundException;
import org.healthystyle.health.service.error.medicine.WeightNegativeOrZeroException;
import org.healthystyle.health.service.medicine.MedicineService;
import org.healthystyle.health.service.medicine.TreatmentService;
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
public class TreatmentTest {
	@Autowired
	private TreatmentService service;
	@MockitoBean
	private HealthService healthService;
	@MockitoBean
	private TreatmentRepository repository;
	@Autowired
	private MedicineService medicineService;
	@MockitoBean
	private MedicineRepository medicineRepository;
	@MockitoBean
	private HealthAccessor accessor;
	@MockitoBean
	private IntakeRepository intakeRepository;
	@MockitoBean
	private PlanRepository planRepository;

	@Test
	public void createMedicineTest() throws ValidationException, MedicineExistException, WeightNegativeOrZeroException,
			ConvertTypeNotRecognizedException, MeasureNotFoundException {
		Health health = new Health(1L);
		health.setId(1L);

		when(accessor.getHealth()).thenReturn(health);

		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
		saveRequest.setName("Грипферон");
		saveRequest.setMeasure(Type.MILLIGRAM);
		saveRequest.setWeight("10");

		Medicine medicine = new Medicine("Грипферон", health);

		when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
		when(medicineRepository.existsByName("Грипферон", 1L)).thenReturn(false);
		medicineService.save(saveRequest);
		verify(medicineRepository, times(1)).save(any(Medicine.class));
	}

	@Test
	public void createMedicineExistTest() throws ValidationException, MedicineExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
		Health health = new Health(1L);
		health.setId(1L);

		when(accessor.getHealth()).thenReturn(health);

		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
		saveRequest.setName("Грипферон");
		saveRequest.setMeasure(Type.MILLIGRAM);
		saveRequest.setWeight("10");

		when(medicineRepository.existsByName("Грипферон", 1L)).thenReturn(true);

		assertThrows(MedicineExistException.class, () -> {
			medicineService.save(saveRequest);
		});
	}

//	@Test
	public void createMedicineWeightIncorrentTest() throws ValidationException, MedicineExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
		Health health = new Health(1L);
		health.setId(1L);

		when(accessor.getHealth()).thenReturn(health);

		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
		saveRequest.setName("Грипферон");
		saveRequest.setMeasure(Type.MILLIGRAM);
		saveRequest.setWeight("-5");

		Medicine medicine = new Medicine("Грипферон", health);

		when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
		medicineService.save(saveRequest);
		verify(medicineRepository, times(1)).save(any(Medicine.class));

		assertThrows(WeightNegativeOrZeroException.class, () -> {
			medicineService.save(saveRequest);
		});
	}

	@Test
	public void createMedicineWeightNotRecognizedTest() throws ValidationException, MedicineExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
		Health health = new Health(1L);
		health.setId(1L);

		when(accessor.getHealth()).thenReturn(health);

		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
		saveRequest.setName("Грипферон");
		saveRequest.setMeasure(Type.MILLIGRAM);
		saveRequest.setWeight("hello");

		Medicine medicine = new Medicine("Грипферон", health);

		when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
		verify(medicineRepository, times(0)).save(any(Medicine.class));

		assertThrows(ConvertTypeNotRecognizedException.class, () -> {
			medicineService.save(saveRequest);
		});
	}

	@Test
	public void createMedicineMeasureNotPointedTest() throws ValidationException, MedicineExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
		Health health = new Health(1L);
		health.setId(1L);

		when(accessor.getHealth()).thenReturn(health);

		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
		saveRequest.setName("Грипферон");
		saveRequest.setMeasure(null);
		saveRequest.setWeight("0");

		Medicine medicine = new Medicine("Грипферон", health);

		when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
		verify(medicineRepository, times(0)).save(any(Medicine.class));

		assertThrows(ValidationException.class, () -> {
			medicineService.save(saveRequest);
		});
	}

	@Test
	public void createMedicineFieldIsNullTest() throws ValidationException, MedicineExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
		Health health = new Health(1L);
		health.setId(1L);

		when(accessor.getHealth()).thenReturn(health);

		MedicineSaveRequest saveRequest = new MedicineSaveRequest();
		saveRequest.setWeight("0");

		Medicine medicine = new Medicine("Грипферон", health);

		when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
		verify(medicineRepository, times(0)).save(any(Medicine.class));

		assertThrows(ValidationException.class, () -> {
			medicineService.save(saveRequest);
		});
	}

	private Health health;
	private Treatment treatment;
	private TreatmentSaveRequest treatmentSaveRequest;

	private Medicine medicine;
	private Plan plan;
	private PlanSaveRequest planSaveRequest1;

	private Intake intake;
	private IntakeSaveRequest intakeSaveRequest1;

	private Measure measureMilligram;

	@BeforeEach
	public void setup() {
		health = new Health(1L);
		health.setId(1L);

		measureMilligram = new Measure(Type.MILLIGRAM);

		treatment = new Treatment("ОРВИ", health);
		treatment.setId(1L);

		medicine = new Medicine("Грипферон", health);
		plan = new Plan(medicine, LocalDate.now(), LocalDate.now().plusWeeks(1), treatment, health);
		plan.setId(1L);

		intake = new Intake(plan, LocalTime.of(8, 30), 1);
		intake.setMeasure(measureMilligram);
		intake.setId(1L);
		intake.setWeight("500");

		treatmentSaveRequest = new TreatmentSaveRequest();
		treatmentSaveRequest.setDescription("ОРВИ");

		planSaveRequest1 = new PlanSaveRequest();
		planSaveRequest1.setMedicineId(1L);
		planSaveRequest1.setStart(LocalDate.now());
		planSaveRequest1.setEnd(LocalDate.now().plusWeeks(1));
		planSaveRequest1.setSequence(Sequence.AFTER_EAT);

		List<PlanSaveRequest> plans = new ArrayList<PlanSaveRequest>();
		plans.add(planSaveRequest1);

		treatmentSaveRequest.setPlans(plans);

		intakeSaveRequest1 = new IntakeSaveRequest();
		intakeSaveRequest1.setDay(1);
		intakeSaveRequest1.setTime(LocalTime.of(8, 30));
		intakeSaveRequest1.setMeasureType(Type.MILLIGRAM);
		intakeSaveRequest1.setWeight("500");
		List<IntakeSaveRequest> intakes = new ArrayList<>();
		intakes.add(intakeSaveRequest1);

		planSaveRequest1.setIntakes(intakes);
	}

	@Test
	public void createTreatmentTest() throws ValidationException, MedicineNotFoundException, TreatmentExistException,
			TreatmentNotFoundException, PlanOverlapsException, IntakeExistException, WeightNegativeOrZeroException,
			ConvertTypeNotRecognizedException, MeasureNotFoundException {
		when(accessor.getHealth()).thenReturn(health);

		when(repository.save(any(Treatment.class))).thenReturn(treatment);
		when(planRepository.save(any(Plan.class))).thenReturn(plan);
		when(intakeRepository.save(any(Intake.class))).thenReturn(intake);

		when(medicineRepository.findById(any(Long.class))).thenReturn(Optional.of(medicine));
		when(repository.findById(1L)).thenReturn(Optional.of(treatment));
		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
		service.save(treatmentSaveRequest);
		verify(repository, times(1)).save(any(Treatment.class));
	}

	@Test
	public void createTreatmentMedicineNotFoundTest() throws ValidationException, MedicineNotFoundException,
			TreatmentExistException, TreatmentNotFoundException, PlanOverlapsException, IntakeExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
		when(accessor.getHealth()).thenReturn(health);

		when(repository.save(any(Treatment.class))).thenReturn(treatment);
		when(planRepository.save(any(Plan.class))).thenReturn(plan);
		when(intakeRepository.save(any(Intake.class))).thenReturn(intake);

		when(medicineRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(repository.findById(1L)).thenReturn(Optional.of(treatment));
		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
		assertThrows(MedicineNotFoundException.class, () -> {
			service.save(treatmentSaveRequest);
		});
	}

	@Test
	public void createTreatmentExistTest() throws ValidationException, MedicineNotFoundException,
			TreatmentExistException, TreatmentNotFoundException, PlanOverlapsException, IntakeExistException,
			WeightNegativeOrZeroException, ConvertTypeNotRecognizedException, MeasureNotFoundException {
		when(accessor.getHealth()).thenReturn(health);

		when(repository.save(any(Treatment.class))).thenReturn(treatment);
		when(planRepository.save(any(Plan.class))).thenReturn(plan);
		when(intakeRepository.save(any(Intake.class))).thenReturn(intake);

		when(repository.existsByDescription("ОРВИ", 1L)).thenReturn(true);

		when(medicineRepository.findById(any(Long.class))).thenReturn(Optional.empty());
		when(repository.findById(1L)).thenReturn(Optional.of(treatment));
		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
		assertThrows(MedicineNotFoundException.class, () -> {
			service.save(treatmentSaveRequest);
		});
	}

//	@Test
	public void createPlanOverlapsTest() throws ValidationException, MedicineNotFoundException, TreatmentExistException,
			TreatmentNotFoundException, PlanOverlapsException, IntakeExistException, WeightNegativeOrZeroException,
			ConvertTypeNotRecognizedException, MeasureNotFoundException {
		when(accessor.getHealth()).thenReturn(health);

		when(repository.save(any(Treatment.class))).thenReturn(treatment);
		when(planRepository.save(any(Plan.class))).thenReturn(plan);
		when(intakeRepository.save(any(Intake.class))).thenReturn(intake);

		when(planRepository.existsOverlaps(LocalDate.now().minusWeeks(1), LocalDate.now().plusWeeks(2), 1L, null))
				.thenReturn(true);

		when(medicineRepository.findById(any(Long.class))).thenReturn(Optional.of(medicine));
		when(repository.findById(1L)).thenReturn(Optional.of(treatment));
		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
		assertThrows(PlanOverlapsException.class, () -> {
			service.save(treatmentSaveRequest);
		});
	}

	@Test
	public void createIntakeExistTest() throws ValidationException, MedicineNotFoundException, TreatmentExistException,
			TreatmentNotFoundException, PlanOverlapsException, IntakeExistException, WeightNegativeOrZeroException,
			ConvertTypeNotRecognizedException, MeasureNotFoundException {
		when(accessor.getHealth()).thenReturn(health);

		when(repository.save(any(Treatment.class))).thenReturn(treatment);
		when(planRepository.save(any(Plan.class))).thenReturn(plan);
		when(intakeRepository.save(any(Intake.class))).thenReturn(intake);

		when(intakeRepository.existsByTimeAndDayAndPlanId(LocalTime.of(8, 30), 1, 1L)).thenReturn(true);

		when(medicineRepository.findById(any(Long.class))).thenReturn(Optional.of(medicine));
		when(repository.findById(1L)).thenReturn(Optional.of(treatment));
		when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
		assertThrows(IntakeExistException.class, () -> {
			service.save(treatmentSaveRequest);
		});
	}
}
