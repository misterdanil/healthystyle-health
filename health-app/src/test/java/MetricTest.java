import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.healthystyle.health.app.Main;
import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.Indicator;
import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.health.model.diet.Food;
import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.model.measure.convert.FloatNumber;
import org.healthystyle.health.model.measure.convert.IntegerNumber;
import org.healthystyle.health.repository.IndicatorRepository;
import org.healthystyle.health.repository.IndicatorTypeRepository;
import org.healthystyle.health.repository.measure.MeasureRepository;
import org.healthystyle.health.repository.measure.convert.ConvertTypeRepository;
import org.healthystyle.health.service.HealthAccessor;
import org.healthystyle.health.service.IndicatorService;
import org.healthystyle.health.service.IndicatorTypeService;
import org.healthystyle.health.service.dto.IndicatorSaveRequest;
import org.healthystyle.health.service.dto.IndicatorTypeSaveRequest;
import org.healthystyle.health.service.error.ValidationException;
import org.healthystyle.health.service.error.indicator.NameExistedException;
import org.healthystyle.health.service.error.measure.MeasureNotFoundException;
import org.healthystyle.health.service.error.measure.convert.ConvertTypeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = Main.class)
public class MetricTest {
	@Autowired
	private IndicatorTypeService indicatorTypeService;
	@MockitoBean
	private IndicatorTypeRepository indicatorTypeRepository;

	@MockitoBean
	private MeasureRepository measureRepository;
	@MockitoBean
	private ConvertTypeRepository convertTypeRepository;

	@Autowired
	private IndicatorService indicatorService;
	@MockitoBean
	private IndicatorRepository indicatorRepository;

	@MockitoBean
	private HealthAccessor accessor;

	private Measure measureKG = new Measure(Type.KG);
	private Measure measureCM = new Measure(Type.CANTIMETRES);
	private ConvertType convertTypeFl = new FloatNumber(1);
	private ConvertType convertTypeInt = new IntegerNumber();

	private IndicatorType indicatorType;
	private IndicatorTypeSaveRequest indicatorTypeSaveRequest;

	private Indicator indicator;
	private IndicatorSaveRequest indicatorSaveRequest;

	private Health health;

	@BeforeEach
	public void setup() {
		health = new Health(1L);

		measureKG.setId(1L);
		measureCM.setId(2L);

		convertTypeFl.setId(1L);
		convertTypeInt.setId(2L);

		indicatorType = new IndicatorType("Вес", measureKG, convertTypeFl, 1L);
		indicatorType.setId(1L);
		indicatorTypeSaveRequest = new IndicatorTypeSaveRequest();
		indicatorTypeSaveRequest.setName(indicatorType.getName());
		indicatorTypeSaveRequest.setType(indicatorType.getMeasure().getType());
		indicatorTypeSaveRequest.setConvertTypeId(1L);

		indicator = new Indicator();
		indicator.setValue("85");
		indicator.setCreatedOn(LocalDateTime.now());

		indicatorSaveRequest = new IndicatorSaveRequest();
		indicatorSaveRequest.setValue(indicator.getValue());
		indicatorSaveRequest.setIndicatorTypeId(indicatorType.getId());
		indicatorSaveRequest.setDate(indicator.getCreatedOn());
	}

	@Test
	public void createIndicatorTypeTest()
			throws ValidationException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException {
		when(accessor.getHealth()).thenReturn(health);

		when(indicatorTypeRepository.save(indicatorType)).thenReturn(indicatorType);
		when(measureRepository.findByType(indicatorType.getMeasure().getType())).thenReturn(measureKG);
		when(convertTypeRepository.findById(1L)).thenReturn(Optional.of(convertTypeFl));
		when(convertTypeRepository.findById(2L)).thenReturn(Optional.of(convertTypeInt));

		indicatorTypeService.save(indicatorTypeSaveRequest);
		verify(indicatorTypeRepository, times(1)).save(any(IndicatorType.class));
	}

	@Test
	public void createIndicatorTypeExistTest()
			throws ValidationException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException {
		when(accessor.getHealth()).thenReturn(health);

		when(indicatorTypeRepository.save(indicatorType)).thenReturn(indicatorType);
		when(measureRepository.findByType(indicatorType.getMeasure().getType())).thenReturn(measureKG);
		when(convertTypeRepository.findById(1L)).thenReturn(Optional.of(convertTypeFl));
		when(convertTypeRepository.findById(2L)).thenReturn(Optional.of(convertTypeInt));
		when(indicatorTypeRepository.existsByName(indicatorType.getName())).thenReturn(true);

		assertThrows(NameExistedException.class, () -> {
			indicatorTypeService.save(indicatorTypeSaveRequest);
		});
	}

	@Test
	public void createIndicatorTypeMeasureIsNotDefinedTest()
			throws ValidationException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException {
		indicatorTypeSaveRequest.setType(null);

		when(accessor.getHealth()).thenReturn(health);

		when(indicatorTypeRepository.save(indicatorType)).thenReturn(indicatorType);
		when(measureRepository.findByType(indicatorType.getMeasure().getType())).thenReturn(measureKG);
		when(convertTypeRepository.findById(1L)).thenReturn(Optional.of(convertTypeFl));
		when(convertTypeRepository.findById(2L)).thenReturn(Optional.of(convertTypeInt));

		assertThrows(ValidationException.class, () -> {
			indicatorTypeService.save(indicatorTypeSaveRequest);
		});
	}

	@Test
	public void createIndicatorTypeConvertTypeIsNotFoundTest()
			throws ValidationException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException {
		indicatorTypeSaveRequest.setConvertTypeId(3L);

		when(accessor.getHealth()).thenReturn(health);

		when(indicatorTypeRepository.save(indicatorType)).thenReturn(indicatorType);
		when(measureRepository.findByType(indicatorType.getMeasure().getType())).thenReturn(measureKG);
		when(convertTypeRepository.findById(1L)).thenReturn(Optional.of(convertTypeFl));
		when(convertTypeRepository.findById(2L)).thenReturn(Optional.of(convertTypeInt));

		assertThrows(ConvertTypeNotFoundException.class, () -> {
			indicatorTypeService.save(indicatorTypeSaveRequest);
		});
	}
	
	@Test
	public void createIndicatorWithoutDateTest()
			throws ValidationException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException {
		indicatorTypeSaveRequest.setConvertTypeId(3L);

		when(accessor.getHealth()).thenReturn(health);

		when(indicatorTypeRepository.save(indicatorType)).thenReturn(indicatorType);
		when(measureRepository.findByType(indicatorType.getMeasure().getType())).thenReturn(measureKG);
		when(convertTypeRepository.findById(1L)).thenReturn(Optional.of(convertTypeFl));
		when(convertTypeRepository.findById(2L)).thenReturn(Optional.of(convertTypeInt));

		assertThrows(ConvertTypeNotFoundException.class, () -> {
			indicatorTypeService.save(indicatorTypeSaveRequest);
		});
	}
	
	@Test
	public void createIndicatorDateFutureTest()
			throws ValidationException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException {
		indicatorTypeSaveRequest.setConvertTypeId(3L);

		when(accessor.getHealth()).thenReturn(health);

		when(indicatorTypeRepository.save(indicatorType)).thenReturn(indicatorType);
		when(measureRepository.findByType(indicatorType.getMeasure().getType())).thenReturn(measureKG);
		when(convertTypeRepository.findById(1L)).thenReturn(Optional.of(convertTypeFl));
		when(convertTypeRepository.findById(2L)).thenReturn(Optional.of(convertTypeInt));

		assertThrows(ConvertTypeNotFoundException.class, () -> {
			indicatorTypeService.save(indicatorTypeSaveRequest);
		});
	}

	@Test
	public void createIndicatorTypeMeasureIsNotFoundTest()
			throws ValidationException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException {
		indicatorTypeSaveRequest.setType(Type.GRAM);

		when(accessor.getHealth()).thenReturn(health);

		when(measureRepository.findByType(Type.KG)).thenReturn(measureKG);
		when(measureRepository.findByType(Type.CANTIMETRES)).thenReturn(measureCM);

		when(indicatorTypeRepository.save(indicatorType)).thenReturn(indicatorType);
		when(measureRepository.findByType(indicatorType.getMeasure().getType())).thenReturn(measureKG);
		when(convertTypeRepository.findById(1L)).thenReturn(Optional.of(convertTypeFl));
		when(convertTypeRepository.findById(2L)).thenReturn(Optional.of(convertTypeInt));

		assertThrows(MeasureNotFoundException.class, () -> {
			indicatorTypeService.save(indicatorTypeSaveRequest);
		});
	}
	
	@Test
	public void createIndicatorConvertTypeWrongFoundTest()
			throws ValidationException, NameExistedException, MeasureNotFoundException, ConvertTypeNotFoundException {
		indicatorTypeSaveRequest.setType(Type.GRAM);

		when(accessor.getHealth()).thenReturn(health);

		when(measureRepository.findByType(Type.KG)).thenReturn(measureKG);
		when(measureRepository.findByType(Type.CANTIMETRES)).thenReturn(measureCM);

		when(indicatorTypeRepository.save(indicatorType)).thenReturn(indicatorType);
		when(measureRepository.findByType(indicatorType.getMeasure().getType())).thenReturn(measureKG);
		when(convertTypeRepository.findById(1L)).thenReturn(Optional.of(convertTypeFl));
		when(convertTypeRepository.findById(2L)).thenReturn(Optional.of(convertTypeInt));

		assertThrows(MeasureNotFoundException.class, () -> {
			indicatorTypeService.save(indicatorTypeSaveRequest);
		});
	}
}
