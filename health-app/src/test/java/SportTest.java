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
import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.model.sport.Exercise;
import org.healthystyle.health.model.sport.Set;
import org.healthystyle.health.model.sport.Sport;
import org.healthystyle.health.model.sport.Step;
import org.healthystyle.health.model.sport.Train;
import org.healthystyle.health.repository.sport.ExerciseRepository;
import org.healthystyle.health.repository.sport.SetRepository;
import org.healthystyle.health.repository.sport.SportRepository;
import org.healthystyle.health.repository.sport.StepRepository;
import org.healthystyle.health.repository.sport.TrainRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.HealthService;
import org.healthystyle.health.service.dto.diet.DietSaveRequest;
import org.healthystyle.health.service.dto.sport.ExerciseSaveRequest;
import org.healthystyle.health.service.dto.sport.SetSaveRequest;
import org.healthystyle.health.service.dto.sport.SportSaveRequest;
import org.healthystyle.health.service.dto.sport.StepSaveRequest;
import org.healthystyle.health.service.dto.sport.TrainSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.diet.ConvertTypeMismatchException;
import org.healthystyle.health.service.error.sport.ExerciseExistException;
import org.healthystyle.health.service.error.sport.ExerciseNotFoundException;
import org.healthystyle.health.service.error.sport.SportNotFoundException;
import org.healthystyle.health.service.error.sport.TrainExistException;
import org.healthystyle.health.service.error.sport.TrainNotFoundException;
import org.healthystyle.health.service.sport.ExerciseService;
import org.healthystyle.health.service.sport.SportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = Main.class)
public class SportTest {
	@Autowired
	private SportService sportService;
	@MockitoBean
	private SportRepository sportRepository;
	@Autowired
	private ExerciseService exerciseService;
	@MockitoBean
	private ExerciseRepository exerciseRepository;
	@MockitoBean
	private StepRepository stepRepository;
	@MockitoBean
	private TrainRepository trainRepository;
	@MockitoBean
	private SetRepository setRepository;
	@MockitoBean
	private HealthAccessor accessor;

	private Exercise exercise;
	private ExerciseSaveRequest exerciseSaveRequest;

	private Step step;
	private StepSaveRequest stepSaveRequest;

	private Sport sport;
	private SportSaveRequest sportSaveRequest = new SportSaveRequest();

	private Train train;
	private TrainSaveRequest trainSaveRequest = new TrainSaveRequest();

	private Set set;
	private SetSaveRequest setSaveRequest = new SetSaveRequest();

	private Health health;

	@BeforeEach
	public void setup() {
		health = new Health(1L);

		exercise = new Exercise("Приседания", health);
		exercise.setId(1L);

		step = new Step("Встать у перкладины", exercise);
		stepSaveRequest = new StepSaveRequest();
		stepSaveRequest.setDescription(step.getDescription());

		exercise.addStep(step);
		exerciseSaveRequest = new ExerciseSaveRequest();
		exerciseSaveRequest.setTitle(exercise.getTitle());

		List<StepSaveRequest> stepSaveRequests = new ArrayList<>();
		stepSaveRequests.add(stepSaveRequest);

		exerciseSaveRequest.setSteps(stepSaveRequests);

		sport = new Sport("Программа сплит", LocalDate.now(), LocalDate.now().plusWeeks(3), health);
		sport.setId(1L);
		train = new Train("Ноги", LocalTime.of(12, 0), 1, sport);
		train.setId(1L);
		trainSaveRequest = new TrainSaveRequest();
		trainSaveRequest.setDescription(train.getDescription());
		trainSaveRequest.setDay(train.getDay());
		trainSaveRequest.setTime(train.getTime());

		set = new Set(exercise, 5, 15, train);
		set.setId(1L);
		setSaveRequest = new SetSaveRequest();
		setSaveRequest.setExerciseId(exercise.getId());
		setSaveRequest.setCount(set.getCount());
		setSaveRequest.setRepeat(set.getRepeat());

		train.addSet(set);
		List<SetSaveRequest> setSaveRequests = new ArrayList<>();
		setSaveRequests.add(setSaveRequest);
		trainSaveRequest.setSets(setSaveRequests);

		sport.addTrain(train);
		sportSaveRequest = new SportSaveRequest();
		sportSaveRequest.setDescription(sport.getDescription());
		sportSaveRequest.setStart(sport.getStart());
		sportSaveRequest.setEnd(sport.getEnd());

		List<TrainSaveRequest> trainSaveRequests = new ArrayList<>();
		trainSaveRequests.add(trainSaveRequest);

		sportSaveRequest.setTrains(trainSaveRequests);

	}

	@Test
	public void createExerciseTest() throws ValidationException, ExerciseExistException, ExerciseNotFoundException {
		when(accessor.getHealth()).thenReturn(health);

		when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);
		when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
		when(stepRepository.save(step)).thenReturn(step);

		exerciseService.save(exerciseSaveRequest);
		verify(exerciseRepository, times(1)).save(any(Exercise.class));
	}

	@Test
	public void createExerciseExistTest()
			throws ValidationException, ExerciseExistException, ExerciseNotFoundException {
		when(accessor.getHealth()).thenReturn(health);

		when(exerciseRepository.save(exercise)).thenReturn(exercise);
		when(stepRepository.save(step)).thenReturn(step);
		when(exerciseRepository.existsByTitle(exercise.getTitle(), health.getId())).thenReturn(true);

		assertThrows(ExerciseExistException.class, () -> {
			exerciseService.save(exerciseSaveRequest);
		});
	}

	@Test
	public void createExerciseValidationTest()
			throws ValidationException, ExerciseExistException, ExerciseNotFoundException {
		exerciseSaveRequest.setTitle(null);

		when(accessor.getHealth()).thenReturn(health);

		when(exerciseRepository.save(exercise)).thenReturn(exercise);
		when(stepRepository.save(step)).thenReturn(step);
		when(exerciseRepository.existsByTitle(exercise.getTitle(), health.getId())).thenReturn(true);

		assertThrows(ValidationException.class, () -> {
			exerciseService.save(exerciseSaveRequest);
		});
	}
	
	@Test
	public void createExerciseStepValidationTest()
			throws ValidationException, ExerciseExistException, ExerciseNotFoundException {
		stepSaveRequest.setDescription(null);

		when(accessor.getHealth()).thenReturn(health);

		when(exerciseRepository.save(exercise)).thenReturn(exercise);
		when(stepRepository.save(step)).thenReturn(step);
		
		when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));

		assertThrows(ValidationException.class, () -> {
			exerciseService.save(exerciseSaveRequest);
		});
	}

	@Test
	public void createSportTest() throws ValidationException, TrainExistException, TrainNotFoundException,
			ExerciseNotFoundException, SportNotFoundException {
		when(accessor.getHealth()).thenReturn(health);

		when(sportRepository.save(any(Sport.class))).thenReturn(sport);
		when(trainRepository.save(any(Train.class))).thenReturn(train);
		when(setRepository.save(any(Set.class))).thenReturn(set);

		when(sportRepository.findById(sport.getId())).thenReturn(Optional.of(sport));
		when(trainRepository.findById(train.getId())).thenReturn(Optional.of(train));
		
		when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));

		sportService.save(sportSaveRequest);
		verify(sportRepository, times(1)).save(any(Sport.class));
	}

	@Test
	public void createSportTrainSetsIsEmptyTest() throws ValidationException, TrainExistException, TrainNotFoundException,
			ExerciseNotFoundException, SportNotFoundException {
		trainSaveRequest.getSets().clear();

		when(accessor.getHealth()).thenReturn(health);

		when(sportRepository.save(any(Sport.class))).thenReturn(sport);
		when(trainRepository.save(any(Train.class))).thenReturn(train);
		when(setRepository.save(any(Set.class))).thenReturn(set);

		when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
		when(trainRepository.findById(train.getId())).thenReturn(Optional.of(train));

		assertThrows(ValidationException.class, () -> {
			sportService.save(sportSaveRequest);
		});
	}
	
	@Test
	public void createSportTrainExistsTest() throws ValidationException, TrainExistException, TrainNotFoundException,
			ExerciseNotFoundException, SportNotFoundException {
		TrainSaveRequest trainSaveRequest2 = new TrainSaveRequest();
		trainSaveRequest2.setDay(trainSaveRequest.getDay());
		trainSaveRequest2.setTime(trainSaveRequest.getTime());
		List<SetSaveRequest> setSaveRequests = new ArrayList<>();
		setSaveRequests.add(setSaveRequest);
		trainSaveRequest2.setSets(setSaveRequests);
		
		sportSaveRequest.getTrains().add(trainSaveRequest2);
		
		when(accessor.getHealth()).thenReturn(health);

		when(sportRepository.save(any(Sport.class))).thenReturn(sport);
		when(trainRepository.save(any(Train.class))).thenReturn(train);
		when(setRepository.save(any(Set.class))).thenReturn(set);
		
		when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));

		when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
		when(trainRepository.findById(train.getId())).thenReturn(Optional.of(train));
		
		assertThrows(TrainExistException.class, () -> {
			sportService.save(sportSaveRequest);
		});
	}
}
