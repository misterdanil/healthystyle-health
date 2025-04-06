package org.healthystyle.health.app;

import org.healthystyle.health.model.Health;
import org.healthystyle.health.model.IndicatorType;
import org.healthystyle.health.model.measure.Measure;
import org.healthystyle.health.model.measure.Type;
import org.healthystyle.health.model.measure.convert.ConvertType;
import org.healthystyle.health.model.measure.convert.FloatNumber;
import org.healthystyle.health.repository.HealthRepository;
import org.healthystyle.health.repository.IndicatorTypeRepository;
import org.healthystyle.health.repository.measure.MeasureRepository;
import org.healthystyle.health.repository.measure.convert.ConvertTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@SpringBootApplication(scanBasePackages = "org.healthystyle.health")
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
		};
	}
}
