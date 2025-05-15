package org.healthystyle.health.app;

import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.repository.HealthRepository;
import org.healthystyle.health.repository.IndicatorTypeRepository;
import org.healthystyle.health.repository.diet.NutritionValueRepository;
import org.healthystyle.health.repository.measure.MeasureRepository;
import org.healthystyle.health.repository.measure.convert.ConvertTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "org.healthystyle.health", "org.healthystyle.util" })
@EnableJpaRepositories(basePackages = "org.healthystyle.health.repository")
@EntityScan(basePackages = "org.healthystyle.health.model")
public class Main {
	@Autowired
	private IndicatorTypeRepository indicatorTypeRepository;
	@Autowired
	private MeasureRepository measureRepository;
	@Autowired
	private ConvertTypeRepository convertTypeRepository;
	@Autowired
	private HealthRepository healthRepository;
	@Autowired
	private NutritionValueRepository nutritionValueRepository;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			// measure
//			Measure measureC = new Measure(Type.CANTIMETRES);
//			measureC = measureRepository.save(measureC);
//			Measure measureK = new Measure(Type.KG);
//			measureK = measureRepository.save(measureK);
//			Measure measureML = new Measure(Type.MOLL_LITRES);
//			measureML = measureRepository.save(measureML);
//
//			// convert type
//			ConvertType ct = new FloatNumber(1);
//			ct = convertTypeRepository.save(ct);
//
//			// indicator types
//			IndicatorType indicatorType = new IndicatorType("Вес", measureK, ct, 1L);
//			indicatorType = indicatorTypeRepository.save(indicatorType);
//			IndicatorType indicatorType2 = new IndicatorType("Рост", measureC, ct, 1L);
//			indicatorType2 = indicatorTypeRepository.save(indicatorType2);
//			IndicatorType indicatorType3 = new IndicatorType("Уровень сахара", measureML, ct, 1L);
//			indicatorType3 = indicatorTypeRepository.save(indicatorType3);
//			
//			// health
//			Health health = new Health(1L);
//			healthRepository.save(health);

			// nutrition values
//			Measure measureGramm = new Measure(Type.GRAM);
//			measureGramm = measureRepository.save(measureGramm);
//			ConvertType ct = convertTypeRepository.findById(1L).get();
//			NutritionValue nv1 = new NutritionValue(Value.CALORIE, measureGramm, ct);
//			NutritionValue nv2 = new NutritionValue(Value.FAT, measureGramm, ct);
//			NutritionValue nv3 = new NutritionValue(Value.CARBOHYDRATE, measureGramm, ct);
//			NutritionValue nv4 = new NutritionValue(Value.PROTEIN, measureGramm, ct);
//			NutritionValue nv5 = new NutritionValue(Value.SUGAR, measureGramm, ct);
//			nutritionValueRepository.save(nv1);
//			nutritionValueRepository.save(nv2);
//			nutritionValueRepository.save(nv3);
//			nutritionValueRepository.save(nv4);
//			nutritionValueRepository.save(nv5);

			// MEDICINE
			Measure measureMilil = new Measure(Type.MILLILITRES);
			Measure measureMililgram = new Measure(Type.MILLIGRAM);
//			measureRepository.save(measureMilil);
//			measureRepository.save(measureMililgram);

		};
	}
}
